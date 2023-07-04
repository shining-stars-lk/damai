package com.tool.multiplesubmitlimit.info.strategy.generateKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: redis-tool
 * @description: 生成键策略上下文
 * @author: k
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
