package com.example.distributecache.repeatLimit.info.strategy.repeatrejected;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: distribute-cache
 * @description: 防重复提交触发时策略上下文
 * @author: lk
 * @create: 2022-05-28
 **/
public class RepeatLimitStrategyContext {

    private static ConcurrentHashMap<String, RepeatLimitHandler> map = new ConcurrentHashMap<>();

    public static void put(String key, RepeatLimitHandler value){
        map.put(key,value);
    }

    public static RepeatLimitHandler get(String key){
        return map.get(key);
    }
}
