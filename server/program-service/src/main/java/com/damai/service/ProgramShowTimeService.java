package com.damai.service;


import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.core.RedisKeyEnum;
import com.damai.dto.ProgramShowTimeAddDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.mapper.ProgramShowTimeMapper;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
import com.damai.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.damai.service.cache.ExpireTime.EXPIRE_TIME;

/**
 * <p>
 * 节目演出时间表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Service
public class ProgramShowTimeService extends ServiceImpl<ProgramShowTimeMapper, ProgramShowTime> {
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramShowTimeMapper programShowTimeMapper;
    
    
    /**
     * 添加节目
     * */
    @Transactional(rollbackFor = Exception.class)
    public Long add(ProgramShowTimeAddDto programShowTimeAddDto) {
        ProgramShowTime programShowTime = new ProgramShowTime();
        BeanUtil.copyProperties(programShowTimeAddDto,programShowTime);
        programShowTime.setId(uidGenerator.getUID());
        programShowTimeMapper.insert(programShowTime);
        return programShowTime.getId();
    }
    
    /**
     * 查询节目演出时间
     * */
    public ProgramShowTime selectProgramShowTimeByProgramId(Long programId){
        ProgramShowTime redisProgramShowTime =
                redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SHOW_TIME,programId),ProgramShowTime.class,() -> {
                    LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                            Wrappers.lambdaQuery(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programId);
                    return Optional.ofNullable(programShowTimeMapper.selectOne(programShowTimeLambdaQueryWrapper))
                            .orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST));
                },EXPIRE_TIME, TimeUnit.DAYS);
        return redisProgramShowTime;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void renewal(){
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                Wrappers.lambdaQuery(ProgramShowTime.class).
                        le(ProgramShowTime::getShowTime, DateUtils.addDay(DateUtils.now(), 1));
        List<ProgramShowTime> programShowTimes = programShowTimeMapper.selectList(programShowTimeLambdaQueryWrapper);
        for (ProgramShowTime programShowTime : programShowTimes) {
            Date oldShowTime = programShowTime.getShowTime();
            Date newShowTime = DateUtils.addMonth(oldShowTime, 1);
            Date newShowDayTime = DateUtils.parseDateTime(DateUtils.formatDate(newShowTime) + " 00:00:00");
            ProgramShowTime updateProgramShowTime = new ProgramShowTime();
            updateProgramShowTime.setShowTime(newShowTime);
            updateProgramShowTime.setShowDayTime(newShowDayTime);
            updateProgramShowTime.setShowWeekTime(DateUtils.getWeekStr(newShowTime));
            LambdaUpdateWrapper<ProgramShowTime> programShowTimeLambdaUpdateWrapper =
                    Wrappers.lambdaUpdate(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programShowTime.getProgramId());
            programShowTimeMapper.update(updateProgramShowTime,programShowTimeLambdaUpdateWrapper);
            redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SHOW_TIME,programShowTime.getProgramId())
                    ,updateProgramShowTime,EXPIRE_TIME, TimeUnit.DAYS);
        }
    }
}
