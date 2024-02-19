package com.damai.context;

import com.damai.config.DelayQueueProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RedissonClient;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 延迟队列 分片
 * @author: 阿宽不是程序员
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
