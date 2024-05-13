package com.damai.repeatexecutelimit.aspect;

import com.damai.constant.LockInfoType;
import com.damai.exception.DaMaiFrameException;
import com.damai.handle.RedissonDataHandle;
import com.damai.locallock.LocalLockCache;
import com.damai.lockinfo.LockInfoHandle;
import com.damai.lockinfo.factory.LockInfoHandleFactory;
import com.damai.repeatexecutelimit.annotion.RepeatExecuteLimit;
import com.damai.servicelock.LockType;
import com.damai.servicelock.ServiceLocker;
import com.damai.servicelock.factory.ServiceLockFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.damai.repeatexecutelimit.constant.RepeatExecuteLimitConstant.PREFIX_NAME;
import static com.damai.repeatexecutelimit.constant.RepeatExecuteLimitConstant.SUCCESS_FLAG;

/**
 /**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 防重复幂等 切面
 * @author: 阿星不是程序员
 **/
@Slf4j
@Aspect
@Order(-11)
@AllArgsConstructor
public class RepeatExecuteLimitAspect {
    
    private final LocalLockCache localLockCache;
    
    private final LockInfoHandleFactory lockInfoHandleFactory;
    
    private final ServiceLockFactory serviceLockFactory;
    
    private final RedissonDataHandle redissonDataHandle;
    
    
    @Around("@annotation(repeatLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatExecuteLimit repeatLimit) throws Throwable {
        long durationTime = repeatLimit.durationTime();
        String message = repeatLimit.message();
        Object obj;
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle(LockInfoType.REPEAT_EXECUTE_LIMIT);
        String lockName = lockInfoHandle.getLockName(joinPoint,repeatLimit.name(), repeatLimit.keys());
        String repeatFlagName = PREFIX_NAME + lockName;
        String flagObject = redissonDataHandle.get(repeatFlagName);
        if (SUCCESS_FLAG.equals(flagObject)) {
            throw new DaMaiFrameException(message);
        }
        ReentrantLock localLock = localLockCache.getLock(lockName,true);
        boolean localLockResult = localLock.tryLock();
        if (!localLockResult) {
            throw new DaMaiFrameException(message);
        }
        try {
            ServiceLocker lock = serviceLockFactory.getLock(LockType.Fair);
            boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, 0);
            if (result) {
                try{
                    flagObject = redissonDataHandle.get(repeatFlagName);
                    if (SUCCESS_FLAG.equals(flagObject)) {
                        throw new DaMaiFrameException(message);
                    }
                    obj = joinPoint.proceed();
                    if (durationTime > 0) {
                        try {
                            redissonDataHandle.set(repeatFlagName,SUCCESS_FLAG,durationTime,TimeUnit.SECONDS);
                        }catch (Exception e) {
                            log.error("getBucket error",e);
                        }
                    }
                    return obj;
                } finally {
                    lock.unlock(lockName);
                }
            }else{
                throw new DaMaiFrameException(message);
            }
        }finally {
            localLock.unlock();
        }
    }
}
