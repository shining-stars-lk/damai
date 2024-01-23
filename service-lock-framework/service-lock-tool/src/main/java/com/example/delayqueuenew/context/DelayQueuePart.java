package com.example.delayqueuenew.context;

import com.example.delayqueuenew.core.ConsumerTask;
import com.example.redisson.RedissonProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RedissonClient;

/**
 * @program: cook-frame
 * @description: 
 * @author: k
 * @create: 2024-01-23
 **/
@Data
@AllArgsConstructor
public class DelayQueuePart {
    
    /**
     * redisson客户端
     * */
    private final RedissonClient redissonClient;
    
    /**
     * 配置信息
     * */
    private RedissonProperties redissonProperties;
    
    /**
     * 客户端对象
     * */
    private final ConsumerTask consumerTask;
}
