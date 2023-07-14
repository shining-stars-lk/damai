package com.example.polymorphism.case2;

import com.example.polymorphism.Fruit;

/**
 * @program: toolkit
 * @description: 讲解简单工厂
 * @author: kuan
 * @create: 2023-06-07
 **/
public class Case2 {
    
    public static void main(String[] args) {
        Fruit fruit1 = FruitFactory.getFruit("apple");
        fruit1.eat();
        Fruit fruit2 = FruitFactory.getFruit("banana");
        fruit2.eat();
        Fruit fruit3 = FruitFactory.getFruit("orange");
        fruit3.eat();
    }
}
