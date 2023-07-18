package com.example.polymorphism;

/**
 * @program: cook-frame
 * @description: 苹果
 * @author: 星哥
 * @create: 2023-06-07
 **/
public class Apple implements Fruit{
    
    @Override
    public void eat() {
        System.out.println("---吃苹果---");
    }
}
