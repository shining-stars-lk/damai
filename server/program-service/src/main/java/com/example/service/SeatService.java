package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.core.RedisKeyEnum;
import com.example.entity.Seat;
import com.example.enums.SeatType;
import com.example.mapper.SeatMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.vo.SeatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.service.cache.ExpireTime.EXPIRE_TIME;

/**
 * <p>
 * 座位表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-11
 */
@Service
public class SeatService extends ServiceImpl<SeatMapper, Seat> {
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private SeatMapper seatMapper;
    
    /**
     * 查询座位
     * */
    public List<SeatVo> selectSeatByProgramId(Long programId) {
        List<SeatVo> seatVoList = new ArrayList<>();
        if (redisCache.hasKey(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LIST, programId))) {
            seatVoList = redisCache.getAllForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LIST, programId),SeatVo.class);
        }else {
            LambdaQueryWrapper<Seat> seatLambdaQueryWrapper = Wrappers.lambdaQuery(Seat.class).eq(Seat::getProgramId, programId);
            List<Seat> seats = seatMapper.selectList(seatLambdaQueryWrapper);
            for (Seat seat : seats) {
                SeatVo seatVo = new SeatVo();
                BeanUtil.copyProperties(seat, seatVo);
                seatVo.setSeatTypeName(SeatType.getMsg(seat.getSeatType()));
                seatVoList.add(seatVo);
            }
            redisCache.putHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LIST, programId)
                    ,seatVoList.stream().collect(Collectors.toMap(s -> String.valueOf(s.getId()),s -> s,(v1,v2) -> v2))
                    ,EXPIRE_TIME, TimeUnit.DAYS);
            return seatVoList;
        }
        return seatVoList;
    }
}
