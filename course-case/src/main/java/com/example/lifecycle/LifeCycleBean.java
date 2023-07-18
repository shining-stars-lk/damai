package com.example.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
//@Component
public class LifeCycleBean implements InitializingBean, DisposableBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet方法执行");
    }
    
    @PostConstruct
    public void postConstruct(){
        System.out.println("postConstruct执行");   
    }

    public LifeCycleBean(){
        System.out.println("LifeCycleBean构造方法执行");
    }
    
    
    
    @Override
    public void destroy() throws Exception {
        System.out.println("destroy方法执行");
    }
    
    @PreDestroy
    public void preDestroy(){
        System.out.println("preDestroy方法执行");
    }
}
