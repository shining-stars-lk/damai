package com.example.context;

import com.example.config.DelayQueueProperties;
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
public class DelayQueueBasePart {
    
    /**
     * redisson客户端
     * */
    private final RedissonClient redissonClient;
    
    /**
     * 配置信息
     * */
    private final DelayQueueProperties delayQueueProperties;
}
