package com.example.distributecache.repeatLimit.info.strategy.generateKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: distribute-cache
 * @description: 生成键策略上下文
 * @author: lk
 * @create: 2022-05-28
 **/
public class GenerateKeyStrategyContext {

    private static ConcurrentHashMap<String, GenerateKeyHandler> map = new ConcurrentHashMap();

    public static void put(String key,GenerateKeyHandler value) {
        map.put(key,value);
    }

    public static GenerateKeyHandler get(String key){
        return map.get(key);
    }
}
