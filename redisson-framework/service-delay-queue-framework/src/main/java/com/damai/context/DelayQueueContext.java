package com.damai.context;

import com.damai.config.DelayQueueProperties;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 延迟队列 发送者上下文
 * @author: 阿宽不是程序员
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
