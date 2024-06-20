package com.damai.service;


import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.core.RedisKeyManage;
import com.damai.dto.ProgramShowTimeAddDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.mapper.ProgramShowTimeMapper;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.cache.local.LocalCacheProgramShowTime;
import com.damai.servicelock.LockType;
import com.damai.servicelock.annotion.ServiceLock;
import com.damai.util.DateUtils;
import com.damai.util.ServiceLockTool;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.damai.core.DistributedLockConstants.GET_PROGRAM_SHOW_TIME_LOCK;
import static com.damai.core.DistributedLockConstants.PROGRAM_SHOW_TIME_LOCK;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目演出时间 service
 * @author: 阿星不是程序员
 **/
@Service
public class ProgramShowTimeService extends ServiceImpl<ProgramShowTimeMapper, ProgramShowTime> {
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramShowTimeMapper programShowTimeMapper;
    
    @Autowired
    private ServiceLockTool serviceLockTool;
    
    @Autowired
    private LocalCacheProgramShowTime localCacheProgramShowTime;
    
    
    @Transactional(rollbackFor = Exception.class)
    public Long add(ProgramShowTimeAddDto programShowTimeAddDto) {
        ProgramShowTime programShowTime = new ProgramShowTime();
        BeanUtil.copyProperties(programShowTimeAddDto,programShowTime);
        programShowTime.setId(uidGenerator.getUid());
        programShowTimeMapper.insert(programShowTime);
        return programShowTime.getId();
    }
    
    public ProgramShowTime selectProgramShowTimeByProgramIdMultipleCache(Long programId){
        return localCacheProgramShowTime.getCache(RedisKeyBuild.createRedisKey
                (RedisKeyManage.PROGRAM_SHOW_TIME, programId).getRelKey(),
                key -> selectProgramShowTimeByProgramId(programId));
    }
    
    @ServiceLock(lockType= LockType.Read,name = PROGRAM_SHOW_TIME_LOCK,keys = {"#programId"})
    public ProgramShowTime selectProgramShowTimeByProgramId(Long programId){
        ProgramShowTime programShowTime = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME, 
                programId), ProgramShowTime.class);
        if (Objects.nonNull(programShowTime)) {
            return programShowTime;
        }
        RLock lock = serviceLockTool.getLock(LockType.Reentrant, GET_PROGRAM_SHOW_TIME_LOCK, 
                new String[]{String.valueOf(programId)});
        lock.lock();
        try {
            programShowTime = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME,
                    programId), ProgramShowTime.class);
            if (Objects.isNull(programShowTime)) {
                LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                        Wrappers.lambdaQuery(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programId);
                programShowTime = Optional.ofNullable(programShowTimeMapper.selectOne(programShowTimeLambdaQueryWrapper))
                        .orElseThrow(() -> new DaMaiFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST));
                redisCache.set(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME, programId),programShowTime
                        ,DateUtils.countBetweenSecond(DateUtils.now(),programShowTime.getShowTime()),TimeUnit.SECONDS);
            }
            return programShowTime;
        }finally {
            lock.unlock();   
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public Set<Long> renewal(){
        Set<Long> programIdSet = new HashSet<>();
        LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                Wrappers.lambdaQuery(ProgramShowTime.class).
                        le(ProgramShowTime::getShowTime, DateUtils.addDay(DateUtils.now(), 1));
        List<ProgramShowTime> programShowTimes = programShowTimeMapper.selectList(programShowTimeLambdaQueryWrapper);
        for (ProgramShowTime programShowTime : programShowTimes) {
            programIdSet.add(programShowTime.getProgramId());
            Date oldShowTime = programShowTime.getShowTime();
            Date newShowTime = DateUtils.addMonth(oldShowTime, 1);
            Date nowDateTime = DateUtils.now();
            while (newShowTime.before(nowDateTime)) {
                newShowTime = DateUtils.addMonth(newShowTime, 1);
            }
            Date newShowDayTime = DateUtils.parseDateTime(DateUtils.formatDate(newShowTime) + " 00:00:00");
            ProgramShowTime updateProgramShowTime = new ProgramShowTime();
            updateProgramShowTime.setShowTime(newShowTime);
            updateProgramShowTime.setShowDayTime(newShowDayTime);
            updateProgramShowTime.setShowWeekTime(DateUtils.getWeekStr(newShowTime));
            LambdaUpdateWrapper<ProgramShowTime> programShowTimeLambdaUpdateWrapper =
                    Wrappers.lambdaUpdate(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programShowTime.getProgramId());
            programShowTimeMapper.update(updateProgramShowTime,programShowTimeLambdaUpdateWrapper);
        }
        return programIdSet;
    }
}
