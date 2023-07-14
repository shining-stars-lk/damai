package com.example.designmode.template;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-09
 **/
public abstract class Fruit {
    
    public abstract String getFruitName();
    
    public abstract void processFruit();
    
    
    public void buyFruit(){
        System.out.println("购买水果 : " + getFruitName());
    }
    
    public void finishEatFruit(){
        System.out.println("吃到了水果 : " + getFruitName());
    }
    
    
    public void eat() {
        buyFruit();
        processFruit();
        finishEatFruit();
    }
}
