package com.damai.context;

import com.damai.config.DelayQueueProperties;
import com.damai.core.ConsumerTask;
import lombok.Data;
import org.redisson.api.RedissonClient;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 延迟队列 消费分片
 * @author: 阿宽不是程序员
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
