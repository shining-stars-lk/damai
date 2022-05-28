package com.example.distributecache.repeatLimit.aspect;

import com.example.distributecache.distributedlock.DistributedLocker;
import com.example.distributecache.distributedlock.redisson.LockType;
import com.example.distributecache.distributedlock.redisson.factory.RedissonLockFactory;
import com.example.distributecache.repeatLimit.annotion.RepeatLimit;
import com.example.distributecache.repeatLimit.info.RepeatLimitInfoProvider;
import com.example.distributecache.repeatLimit.info.RepeatRejectedStrategy;
import com.example.distributecache.repeatLimit.info.strategy.repeatrejected.RepeatLimitHandler;
import com.example.distributecache.repeatLimit.info.strategy.repeatrejected.RepeatLimitStrategyFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 防重复提交限制功能
 * @author: lk
 * @create: 2022-05-28
 **/
@Component
@Aspect
@Order(-9)
public class RepeatLimitAspect {

    private final Logger logger = LoggerFactory.getLogger(RepeatLimitAspect.class);

    private final long MAX_TIME_OUT = 15;

    private final long WAIT_TIME = 0;

    @Autowired
    private RepeatLimitInfoProvider repeatLimitInfoProvider;

    @Autowired
    private RedissonLockFactory redissonLockFactory;

    @Autowired
    private RepeatLimitStrategyFactory repeatLimitStrategyFactory;

    @Around("@annotation(repeatLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatLimit repeatLimit) throws Throwable {
        Object o = null;
        String lockName = "";
        String resultKeyName = "";
        String[] keys = repeatLimit.keys();
        String name = repeatLimit.name();
        long timeout = checkTimeOut(repeatLimit.timeout());
        RepeatRejectedStrategy repeatRejectedStrategy = repeatLimit.repeatRejected();
        if (keys != null && keys.length > 0) {
            lockName = repeatLimitInfoProvider.getLockName(joinPoint,name, keys);
            resultKeyName = repeatLimitInfoProvider.getResultKeyName(joinPoint,name, keys);
        }else{
            lockName = repeatLimitInfoProvider.getLockNameByGenerateKeyStrategy(repeatLimit, joinPoint);
            resultKeyName = repeatLimitInfoProvider.getResultKeyNameByGenerateKeyStrategy(repeatLimit, joinPoint);
        }

        logger.info("==repeat limit lockName:{} resultKeyName:{}==",lockName,resultKeyName);
        RepeatLimitHandler repeatLimitHandler = repeatLimitStrategyFactory.getRepeatLimitStrategy(repeatRejectedStrategy.getMsg());

        DistributedLocker lock = redissonLockFactory.createLock(LockType.Reentrant);
        boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, WAIT_TIME);
        //加锁成功执行
        if (result) {
            try{
                o = repeatLimitHandler.execute(resultKeyName, timeout,TimeUnit.SECONDS,joinPoint);
                return o;
            }finally {
                lock.unlock(lockName);
            }
        //加锁没有成功
        }else{
            o = repeatLimitHandler.repeatRejected(resultKeyName);
            return o;
        }
    }

    public long checkTimeOut(long timeout){
        if (timeout > MAX_TIME_OUT) {
            timeout = MAX_TIME_OUT;
        }
        return timeout;
    }
}
