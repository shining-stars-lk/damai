package com.tool.servicelock.aspect;

import com.example.core.StringUtil;
import com.tool.servicelock.ServiceLockInfoProvider;
import com.tool.servicelock.ServiceLocker;
import com.tool.servicelock.annotion.ServiceLock;
import com.tool.servicelock.redisson.LockType;
import com.tool.servicelock.redisson.factory.ServiceLockFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: 分布式锁切面
 * @author: k
 * @create: 2022-05-28
 **/
@Aspect
@Order(-10)
public class ServiceLockAspect {

    private final Logger logger = LoggerFactory.getLogger(ServiceLockAspect.class);

    
    private ServiceLockFactory serviceLockFactory;
    
    private ServiceLockInfoProvider serviceLockInfoProvider;
    
    public ServiceLockAspect(ServiceLockFactory serviceLockFactory, ServiceLockInfoProvider serviceLockInfoProvider){
        this.serviceLockFactory = serviceLockFactory;
        this.serviceLockInfoProvider = serviceLockInfoProvider;
    }


    @Around("@annotation(servicelock)")
    public Object around(ProceedingJoinPoint joinPoint, ServiceLock servicelock) throws Throwable {
        String lockName = serviceLockInfoProvider.getLockName(joinPoint, servicelock.name(),servicelock.keys());
        LockType lockType = servicelock.lockType();
        long waitTime = servicelock.waitTime();
        TimeUnit timeUnit = servicelock.timeUnit();

        ServiceLocker lock = serviceLockFactory.createLock(lockType);
        boolean result = lock.tryLock(lockName, timeUnit, waitTime);

        if (result) {
            try {
                return joinPoint.proceed();
            }finally{
                lock.unlock(lockName);
            }
        }else {
            logger.warn("Timeout while acquiring serviceLock:{}",lockName);
            //加锁失败,如果设置了自定义处理，则执行
            String customLockTimeoutStrategy = servicelock.customLockTimeoutStrategy();
            if (StringUtil.isNotEmpty(customLockTimeoutStrategy)) {
                return handleCustomLockTimeoutStrategy(customLockTimeoutStrategy, joinPoint);
            }else{
                //默认处理
                servicelock.lockTimeoutStrategy().handler(lockName);
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
