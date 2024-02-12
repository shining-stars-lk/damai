package com.example.handle;

import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-07
 **/
@AllArgsConstructor
public class RedissonDataHandle {
    
    private final RedissonClient redissonClient;
    
    public Object get(String key){
        return redissonClient.getBucket(key).get();
    }
    
    public void set(String key,String value){
        redissonClient.getBucket(key).set(value);
    }
    
    public void set(String key,String value,long timeToLive, TimeUnit timeUnit){
        redissonClient.getBucket(key).set(value,timeToLive,timeUnit);
    }
}
