package com.example.distributecache.distributedlock.annotion;

import com.example.distributecache.distributedlock.info.LockTimeOutStrategy;
import com.example.distributecache.distributedlock.redisson.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 分布式锁注解
 * @author: lk
 * @create: 2022-05-28
 **/
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的类型(默认 可重入锁)
     * */
    LockType lockType() default LockType.Reentrant;
    /**
     * 业务名称
     * @return name
     */
    String name() default "";
    /**
     * 自定义业务key
     * @return keys
     */
    String [] keys();

    /**
     * 尝试加锁失败最多等待时间
     * @return waitTime
     */
    long waitTime() default 10;

    /**
     * 时间单位
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 加锁超时的处理策略
     * @return LockTimeOutStrategy
     */
    LockTimeOutStrategy lockTimeoutStrategy() default LockTimeOutStrategy.FAIL;

    /**
     * 自定义加锁超时的处理策略
     * @return customLockTimeoutStrategy
     */
    String customLockTimeoutStrategy() default "";
}
