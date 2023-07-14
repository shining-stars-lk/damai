package com.tool.delayqueue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-19
 **/
@AllArgsConstructor
@Slf4j
public class Consumer {
    
    private RedissonClient redissonClient;
    
    public void consumeMessage(String topic, ExecuteConsumer executeConsumer){
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(topic);
        new Thread(() -> {
            for (;;) {
                try {
                    String message = blockingDeque.take();
                    executeConsumer.consumer(message);
                } catch (InterruptedException e) {
                    log.error("consume error");
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
