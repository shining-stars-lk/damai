package com.example.listenerevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-09
 **/
@Component
public class TestEventPush {

    @Autowired
    private ApplicationContext applicationContext;
    
    @PostConstruct
    public void postConstruct(){
        applicationContext.publishEvent(new TestEvent("testEvent事件"));
    }
}
