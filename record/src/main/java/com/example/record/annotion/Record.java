package com.example.record.annotion;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author lk
 * Content :记录注解
 */
@Target(value= {ElementType.TYPE, ElementType.METHOD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Record {
    
    /**
     * 操作名称
     * */
    String operatorName() default "";
    
    /**
     * 记录内容 
     * 例如：
     * 名字为{getDoctorUserName}的医生,将名字为{getPatientUserName}的患者的订单,添加了备注为(#dto.remark)
     * 
     * 说明1：
     * {}中的内容为方法名，
     * getDoctorUserName和getPatientUserName为方法调用，在使用@Record注解同一类中添加此方法，入参和使用@Record注解的方法相同，返回值必须为String类型
     * 
     * 说明2：
     * ()中的内容为参数名
     * #dto.remark #为固定前缀，dto为入参名，remark为参数中的字段名
     * @return name
     */
    String content() default "";
    
}
