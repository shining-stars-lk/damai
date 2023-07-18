package com.example.polymorphism;

/**
 * @program: cook-frame
 * @description: 香蕉
 * @author: 星哥
 * @create: 2023-06-07
 **/
public class Banana implements Fruit{
    @Override
    public void eat() {
        System.out.println("---吃香蕉---");
    }
}
