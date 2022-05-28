package com.example.distributecache.repeatLimit.annotion;

import com.example.distributecache.repeatLimit.info.GenerateKeyStrategy;
import com.example.distributecache.repeatLimit.info.RepeatRejectedStrategy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @program: distribute-cache
 * @description: 防重复提交限制注解
 * @author: lk
 * @create: 2022-05-28
 **/
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface RepeatLimit {

    /**
     * 业务名称
     * @return name
     */
    String name() default "";
    /**
     * key设置
     * @return key
     */
    String [] keys() default {};

    /**
     * 如果不指定key，则执行现有的生成key策略
     * */
    GenerateKeyStrategy generatorKey() default GenerateKeyStrategy.PARAMETER_GENERATE_KEY_STRATEGY;
    /**
     * 在设置的时间内不允许重复调用或者返回第一次调用结果(默认10s,最大允许设置15s)
     * @return timeout
     */
    long timeout() default 10;

    /**
     * 提交重复时，执行的策略(默认快速拒绝)
     * */
    RepeatRejectedStrategy repeatRejected() default RepeatRejectedStrategy.ABORT_STRATEGY;
}
