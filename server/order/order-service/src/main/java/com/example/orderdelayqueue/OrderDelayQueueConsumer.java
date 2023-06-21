package com.example.orderdelayqueue;

import com.example.mapper.OrderMapper;
import com.tool.delayqueue.Consumer;
import com.tool.servicelock.redisson.RedissonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-21
 **/
@Component
public class OrderDelayQueueConsumer {

    @Autowired
    private Consumer consumer;
    
    @Autowired
    private RedissonProperties redissonProperties;
    
    @Autowired
    private OrderMapper orderMapper;
    
    /**
     * 延迟队列消费消息
     * */
    @PostConstruct
    public void orderConsumer(){
        consumer.consumeMessage(redissonProperties.getConsumeTopic(),message -> {
            String id = message;
            orderMapper.timeOutCancelOrder(id);
        });
    }
}
