package com.tool.multiplesubmitlimit.annotion;

import com.tool.multiplesubmitlimit.info.GenerateKeyStrategy;
import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitRejectedStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @program: redis-tool
 * @description: 防重复提交限制注解
 * @author: 星哥
 * @create: 2023-05-28
 **/
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface MultipleSubmitLimit {

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
     * 在设置的时间内不允许重复调用或者返回第一次调用结果(默认0s,最大允许设置15s,不使用此配置，不允许重复调用的时间为方法执行时间，
     *  如果使用此配置，则以此配置为准，要注意方法执行时间要小于此配置时间，否则失去了原子性)
     * @return timeout
     */
    long timeout() default 10;

    /**
     * 提交重复时，执行的策略(默认快速拒绝)
     * */
    MultipleSubmitLimitRejectedStrategy repeatRejected() default MultipleSubmitLimitRejectedStrategy.ABORT_STRATEGY;
}
