package com.example.service;


import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.core.RedisKeyEnum;
import com.example.dto.TicketCategoryAddDto;
import com.example.entity.TicketCategory;
import com.example.mapper.TicketCategoryMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.vo.TicketCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.service.cache.ExpireTime.EXPIRE_TIME;

/**
 * <p>
 * 节目票档表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Service
public class TicketCategoryService extends ServiceImpl<TicketCategoryMapper, TicketCategory> {
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private TicketCategoryMapper ticketCategoryMapper;
    
    @Transactional(rollbackFor = Exception.class)
    public Long add(TicketCategoryAddDto ticketCategoryAddDto) {
        TicketCategory ticketCategory = new TicketCategory();
        BeanUtil.copyProperties(ticketCategoryAddDto,ticketCategory);
        ticketCategory.setId(uidGenerator.getUID());
        ticketCategoryMapper.insert(ticketCategory);
        return ticketCategory.getId();
    }
    
    public List<TicketCategoryVo> selectTicketCategoryListByProgramId(Long programId){
        return redisCache.getValueIsList(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_CATEGORY_LIST, programId), TicketCategoryVo.class, () -> {
                    LambdaQueryWrapper<TicketCategory> ticketCategoryLambdaQueryWrapper = Wrappers.lambdaQuery(TicketCategory.class)
                            .eq(TicketCategory::getProgramId, programId);
                    List<TicketCategory> ticketCategoryList = ticketCategoryMapper.selectList(ticketCategoryLambdaQueryWrapper);
                    return ticketCategoryList.stream().map(ticketCategory -> {
                        ticketCategory.setRemainNumber(null);
                        TicketCategoryVo ticketCategoryVo = new TicketCategoryVo();
                        BeanUtil.copyProperties(ticketCategory, ticketCategoryVo);
                        return ticketCategoryVo;
                    }).collect(Collectors.toList());
                }, EXPIRE_TIME, TimeUnit.DAYS);
    }
    
    /**
     * 设置余票数量
     * */
    public void setRedisRemainNumber(Long programId){
        Boolean exist = redisCache.hasKey(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId));
        if (!exist) {
            LambdaQueryWrapper<TicketCategory> ticketCategoryLambdaQueryWrapper = Wrappers.lambdaQuery(TicketCategory.class)
                    .eq(TicketCategory::getProgramId, programId);
            List<TicketCategory> ticketCategoryList = ticketCategoryMapper.selectList(ticketCategoryLambdaQueryWrapper);
            Map<String, Long> map = ticketCategoryList.stream().collect(Collectors.toMap(t -> String.valueOf(t.getId()),
                    TicketCategory::getRemainNumber, (v1, v2) -> v2));
            redisCache.putHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId),map);
        }
    }
}
