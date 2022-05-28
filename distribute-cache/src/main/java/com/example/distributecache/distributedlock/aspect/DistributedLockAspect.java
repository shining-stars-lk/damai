package com.example.distributecache.distributedlock.aspect;

import com.example.distributecache.core.StringUtil;
import com.example.distributecache.distributedlock.DistributedLockInfoProvider;
import com.example.distributecache.distributedlock.DistributedLocker;
import com.example.distributecache.distributedlock.annotion.DistributedLock;
import com.example.distributecache.distributedlock.redisson.LockType;
import com.example.distributecache.distributedlock.redisson.factory.RedissonLockFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 分布式锁切面
 * @author: lk
 * @create: 2022-05-28
 **/

@Component
@Aspect
@Order(-10)
public class DistributedLockAspect {

    private final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Autowired
    private RedissonLockFactory redissonLockFactory;
    @Autowired
    private DistributedLockInfoProvider distributedLockInfoProvider;


    @Around("@annotation(distributedlock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedlock) throws Throwable {
        String lockName = distributedLockInfoProvider.getLockName(joinPoint, distributedlock.name(),distributedlock.keys());
        LockType lockType = distributedlock.lockType();
        long waitTime = distributedlock.waitTime();
        TimeUnit timeUnit = distributedlock.timeUnit();

        DistributedLocker lock = redissonLockFactory.createLock(lockType);
        boolean result = lock.tryLock(lockName, timeUnit, waitTime);

        if (result) {
            try {
                return joinPoint.proceed();
            }finally{
                lock.unlock(lockName);
            }
        }else {
            logger.warn("Timeout while acquiring distributedLock:{}",lockName);
            //加锁失败,如果设置了自定义处理，则执行
            String customLockTimeoutStrategy = distributedlock.customLockTimeoutStrategy();
            if (StringUtil.isNotEmpty(customLockTimeoutStrategy)) {
                return handleCustomLockTimeoutStrategy(customLockTimeoutStrategy, joinPoint);
            }else{
                //默认处理
                distributedlock.lockTimeoutStrategy().handler(lockName);
            }
            return joinPoint.proceed();
        }
    }

    public Object handleCustomLockTimeoutStrategy(String customLockTimeoutStrategy,JoinPoint joinPoint) {
        // prepare invocation context
        Method currentMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Method handleMethod = null;
        try {
            handleMethod = target.getClass().getDeclaredMethod(customLockTimeoutStrategy, currentMethod.getParameterTypes());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Illegal annotation param customLockTimeoutStrategy :" + customLockTimeoutStrategy,e);
        }
        Object[] args = joinPoint.getArgs();

        // invoke
        Object result;
        try {
            result = handleMethod.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Fail to illegal access custom lock timeout handler: " + customLockTimeoutStrategy ,e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Fail to invoke custom lock timeout handler: " + customLockTimeoutStrategy ,e);
        }
        return result;
    }
}
