package com.damai.servicelock.annotion;

import com.damai.servicelock.LockType;
import com.damai.servicelock.info.LockTimeOutStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分布式锁 注解
 * @author: 阿星不是程序员
 **/
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ServiceLock {

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
