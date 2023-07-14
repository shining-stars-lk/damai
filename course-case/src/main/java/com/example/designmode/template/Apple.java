package com.example.designmode.template;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-09
 **/
public class Apple extends Fruit{
    @Override
    public String getFruitName() {
        return "苹果";
    }
    
    @Override
    public void processFruit() {
        System.out.println("将苹果的皮用刀削掉");
    }
}
