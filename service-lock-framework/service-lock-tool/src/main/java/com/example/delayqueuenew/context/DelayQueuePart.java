package com.example.delayqueuenew.context;

import com.example.delayqueuenew.core.ConsumerTask;
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
    
    private final RedissonClient redissonClient;
    
    /**
     * 从队列拉取数据的线程数量，如果业务过慢可调大
     * */
    private final Integer threadCount;
    
    /**
     * 延时队列的隔离分区数，延时有瓶颈时 可调大次数，但会增大redis的cpu消耗
     * */
    private final Integer isolationRegionCount;
    /**
     * 客户端对象
     * */
    private final ConsumerTask consumerTask;
    
    
    private String relTopic;
    
    public DelayQueuePart(RedissonClient redissonClient,Integer threadCount,Integer isolationRegionCount,ConsumerTask consumerTask){
        this.redissonClient = redissonClient;
        this.threadCount = threadCount;
        this.isolationRegionCount = isolationRegionCount;
        this.consumerTask = consumerTask;
    }
}
