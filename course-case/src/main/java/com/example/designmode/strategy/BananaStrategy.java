package com.example.designmode.strategy;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
public class BananaStrategy implements FruitStrategy{
    @Override
    public void eat() {
        System.out.println("吃到了香蕉");
    }
}
