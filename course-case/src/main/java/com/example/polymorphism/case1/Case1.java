package com.example.polymorphism.case1;

import com.example.polymorphism.Apple;
import com.example.polymorphism.Banana;
import com.example.polymorphism.Fruit;

/**
 * @program: toolkit
 * @description: 讲解多态
 * @author: kuan
 * @create: 2023-06-07
 **/
public class Case1 {
    
    public static void main(String[] args) {
        Fruit fruit1 = new Apple();
        Fruit fruit2 = new Banana();
        fruit1.eat();
        fruit2.eat();
    }
}
