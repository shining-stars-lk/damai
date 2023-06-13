package com.example.circularDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class B {

    @Autowired
    private A a;
    
    @PostConstruct
    public void test(){
        System.out.println("获取B对象中的a属性:" + a);
    }
}
