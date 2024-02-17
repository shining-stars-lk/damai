package com.damai.context;

import com.damai.config.DelayQueueProperties;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
public class DelayQueueContext {
    
    private final DelayQueueProperties delayQueueProperties;
    
    private final RedissonClient redissonClient;
    
    private final Map<String, DelayQueueProduceCombine> delayQueueProduceCombineMap = new ConcurrentHashMap<>();
    
    public DelayQueueContext(DelayQueueProperties delayQueueProperties,RedissonClient redissonClient){
        this.delayQueueProperties = delayQueueProperties;
        this.redissonClient = redissonClient;
    }
    
    public void sendMessage(String topic,String content,long delayTime, TimeUnit timeUnit) {
        DelayQueueProduceCombine delayQueueProduceCombine = delayQueueProduceCombineMap.computeIfAbsent(topic,k -> {
            DelayQueueBasePart delayQueueBasePart = new DelayQueueBasePart(redissonClient,delayQueueProperties);
            return new DelayQueueProduceCombine(delayQueueBasePart,topic);
        });
        delayQueueProduceCombine.offer(content,delayTime,timeUnit);
    }
}
