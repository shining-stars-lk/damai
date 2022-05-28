package com.example.distributecache.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: distribute-cache
 * @description: spring工具类
 * @author: lk
 * @create: 2022-05-28
 **/
@Component
public class SpringUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

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
}
