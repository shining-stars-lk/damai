package com.damai.handle;

import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redisson操作
 * @author: 阿星不是程序员
 **/
@AllArgsConstructor
public class RedissonDataHandle {
    
    private final RedissonClient redissonClient;
    
    public String get(String key){
        return (String)redissonClient.getBucket(key).get();
    }
    
    public void set(String key,String value){
        redissonClient.getBucket(key).set(value);
    }
    
    public void set(String key,String value,long timeToLive, TimeUnit timeUnit){
        redissonClient.getBucket(key).set(value,getDuration(timeToLive,timeUnit));
    }
    
    public Duration getDuration(long timeToLive, TimeUnit timeUnit){
        switch (timeUnit) {
            
            case MINUTES -> {
                return Duration.ofMinutes(timeToLive);
            }
            
            case HOURS -> {
                return Duration.ofHours(timeToLive);
            }
            
            case DAYS -> {
                return Duration.ofDays(timeToLive);
            }
            
            default -> {
                return Duration.ofSeconds(timeToLive);
            }
        }
    }
}
