package com.example.context;

import com.example.config.DelayQueueProperties;
import com.example.core.ConsumerTask;
import lombok.Data;
import org.redisson.api.RedissonClient;

/**
 * @program: cook-frame
 * @description: 
 * @author: k
 * @create: 2024-01-23
 **/
@Data
public class DelayQueuePart extends DelayQueueBasePart {
    
    /**
     * 客户端对象
     * */
    private final ConsumerTask consumerTask;
    
    public DelayQueuePart(RedissonClient redissonClient, DelayQueueProperties delayQueueProperties, ConsumerTask consumerTask){
        super(redissonClient,delayQueueProperties);
        this.consumerTask = consumerTask;
    }
}
