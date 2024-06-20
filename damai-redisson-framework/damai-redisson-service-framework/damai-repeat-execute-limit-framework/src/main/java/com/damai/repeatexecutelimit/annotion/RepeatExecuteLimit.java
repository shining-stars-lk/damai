package com.damai.repeatexecutelimit.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 防重复幂等 注解
 * @author: 阿星不是程序员
 **/
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface RepeatExecuteLimit {

    /**
     * 业务名称
     * @return name
     */
    String name() default "";
    /**
     * key设置
     * @return key
     */
    String [] keys();
    
    /**
     * 在多长时间内一直保持幂等，如果不配置则以执行方法为准
     * */
    long durationTime() default 0L;

    /**
     * 当消息执行已经出发防重复执行的限制时，提示信息
     * */
    String message() default "提交频繁，请稍后重试";
    
}
