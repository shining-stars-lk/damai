package com.damai.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: spring工具
 * @author: 阿宽不是程序员
 **/
@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
    
    private ApplicationContext applicationContext;
    
    private Environment environment;
    
    private static SpringUtil springUtil;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @PostConstruct
    public void init(){
        springUtil = this;
    }
    
    public static ApplicationContext getApplicationContext(){
        return springUtil.applicationContext;
    }
    
    
    public static <T> T getBean(Class<T> requiredType){
        if (springUtil == null) {
            return null;
        }
        return springUtil.applicationContext.getBean(requiredType);
    }
    
    public static String getProperty(String key){
        if (springUtil == null) {
            return null;
        }
        return springUtil.environment.getProperty(key);
    }
    
    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }
    
    
}
