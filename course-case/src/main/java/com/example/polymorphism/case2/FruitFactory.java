package com.example.polymorphism.case2;

import com.example.polymorphism.Apple;
import com.example.polymorphism.Banana;
import com.example.polymorphism.Fruit;

/**
 * @program: toolkit
 * @description: 水果工厂
 * @author: k
 * @create: 2023-06-07
 **/
public class FruitFactory {
    
    public static Fruit getFruit(String fruitName){
        if ("apple".equals(fruitName)) {
            return new Apple();
        }
        if ("banana".equals(fruitName)) {
            return new Banana();
        }
        throw new RuntimeException("没有找到对应的水果");
    }
}
