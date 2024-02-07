package com.example.repeatexecutelimit.aspect;

import com.example.exception.CookFrameException;
import com.example.handle.RedissonDataHandle;
import com.example.locallock.LocalLockCache;
import com.example.lockinfo.LockInfoHandle;
import com.example.lockinfo.LockInfoType;
import com.example.lockinfo.factory.LockInfoHandleFactory;
import com.example.repeatexecutelimit.annotion.RepeatExecuteLimit;
import com.example.servicelock.LockType;
import com.example.servicelock.ServiceLocker;
import com.example.servicelock.factory.ServiceLockFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.repeatexecutelimit.constant.RepeatExecuteLimitConstant.PREFIX_NAME;
import static com.example.repeatexecutelimit.constant.RepeatExecuteLimitConstant.SUCCESS_FLAG;

/**
 * @program: redis-example
 * @description: 防重复提交限制功能
 * @author: 星哥
 * @create: 2023-05-28
 **/
@Slf4j
@Aspect
@Order(-9)
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
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle(LockInfoType.SERVICE_LOCK);
        String lockName = lockInfoHandle.getLockName(joinPoint,repeatLimit.name(), repeatLimit.keys());
        String repeatFlagName = PREFIX_NAME + lockName;
        Object flagObject;
        if (durationTime > 0) {
            flagObject = redissonDataHandle.get(repeatFlagName);
            if (SUCCESS_FLAG.equals(flagObject)) {
                throw new CookFrameException(message);
            }
        }
        ReentrantLock localLock = localLockCache.getLock(lockName,false);
        boolean localLockResult = localLock.tryLock();
        if (!localLockResult) {
            throw new CookFrameException(message);
        }
        try {
            ServiceLocker lock = serviceLockFactory.getLock(LockType.Reentrant);
            boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, 0);
            //加锁成功执行
            if (result) {
                try{
                    flagObject = redissonDataHandle.get(repeatFlagName);
                    if (SUCCESS_FLAG.equals(flagObject)) {
                        throw new CookFrameException(message);
                    }
                    obj = joinPoint.proceed();
                    try {
                        redissonDataHandle.set(repeatFlagName,SUCCESS_FLAG,durationTime,TimeUnit.SECONDS);
                    }catch (Exception e) {
                        log.error("getBucket error",e);
                    }
                    return obj;
                } finally {
                    lock.unlock(lockName);
                }
            }else{
                throw new CookFrameException(message);
            }
        }finally {
            localLock.unlock();
        }
    }
}
