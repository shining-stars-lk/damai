package com.example.circularDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class A {

    @Autowired
    private B b;

    @PostConstruct
    public void test(){
        System.out.println("获取A对象中的b属性:" + b);
    }
}