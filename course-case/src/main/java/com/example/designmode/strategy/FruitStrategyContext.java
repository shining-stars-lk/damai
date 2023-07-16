package com.example.designmode.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
public class FruitStrategyContext {

    private static Map<String,FruitStrategy> map = new HashMap();

    static {
        map.put("苹果",new AppleStrategy());
        map.put("香蕉",new BananaStrategy());
    }
    
    public static FruitStrategy getFruitStrategy(String fruitName) {
        return map.get(fruitName);
    }
}
