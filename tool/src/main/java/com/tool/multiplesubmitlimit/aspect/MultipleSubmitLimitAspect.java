package com.tool.multiplesubmitlimit.aspect;

import com.tool.multiplesubmitlimit.annotion.MultipleSubmitLimit;
import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitInfo;
import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitRejectedStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitHandler;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitStrategyFactory;
import com.tool.servicelock.ServiceLocker;
import com.tool.redisson.LockType;
import com.tool.redisson.factory.ServiceLockFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: 防重复提交限制功能
 * @author: 星哥
 * @create: 2023-05-28
 **/
@Slf4j
@Aspect
@Order(-9)
@AllArgsConstructor
public class MultipleSubmitLimitAspect {
    
    private final MultipleSubmitLimitInfo multipleSubmitLimitInfo;
    
    private final ServiceLockFactory serviceLockFactory;
    
    private final MultipleSubmitLimitStrategyFactory multipleSubmitLimitStrategyFactory;
    
    
    @Around("@annotation(repeatLimit)")
    public Object around(ProceedingJoinPoint joinPoint, MultipleSubmitLimit repeatLimit) throws Throwable {
        Object obj;
        String lockName;
        String resultKeyName;
        String[] keys = repeatLimit.keys();
        String name = repeatLimit.name();
        long timeout = checkTimeOut(repeatLimit.timeout());
        MultipleSubmitLimitRejectedStrategy repeatRejectedStrategy = repeatLimit.repeatRejected();
        if (keys != null && keys.length > 0) {
            lockName = multipleSubmitLimitInfo.getLockName(joinPoint,name, keys);
            resultKeyName = multipleSubmitLimitInfo.getResultKeyName(joinPoint,name, keys);
        }else{
            lockName = multipleSubmitLimitInfo.getLockNameByGenerateKeyStrategy(repeatLimit, joinPoint);
            resultKeyName = multipleSubmitLimitInfo.getResultKeyNameByGenerateKeyStrategy(repeatLimit, joinPoint);
        }

        log.info("==repeat limit lockName:{} resultKeyName:{}==",lockName,resultKeyName);
        MultipleSubmitLimitHandler repeatLimitHandler = multipleSubmitLimitStrategyFactory.getMultipleSubmitLimitStrategy(repeatRejectedStrategy.getMsg());

        ServiceLocker lock = serviceLockFactory.getLock(LockType.Reentrant);
        boolean result;
        long WAIT_TIME = 0;
        if (timeout == 0) {
            result = lock.tryLock(lockName, TimeUnit.SECONDS, WAIT_TIME);
        }else {
            result = lock.tryLock(lockName, TimeUnit.SECONDS, WAIT_TIME, timeout);
        }
        //加锁成功执行
        if (result) {
            try{
                obj = repeatLimitHandler.execute(resultKeyName, timeout,TimeUnit.SECONDS,joinPoint);
                return obj;
            }finally {
                lock.unlock(lockName);
            }
        //加锁没有成功
        }else{
            obj = repeatLimitHandler.repeatRejected(resultKeyName);
            return obj;
        }
    }

    public long checkTimeOut(long timeout){
        long MAX_TIME_OUT = 15;
        if (timeout > MAX_TIME_OUT) {
            timeout = MAX_TIME_OUT;
        }
        return timeout;
    }
}
