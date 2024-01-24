package com.example.delayconsumer;

import lombok.AllArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-19
 **/
@AllArgsConstructor
public class Producer {

    private RedissonClient redissonClient;
    
    public boolean produceMessage(String topic,String message,long delay, TimeUnit timeUnit){
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(topic);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(message,delay,timeUnit);
        return true;
    }
}
