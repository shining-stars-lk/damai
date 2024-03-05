package com.damai.config;


import com.damai.context.DelayQueueBasePart;
import com.damai.context.DelayQueueContext;
import com.damai.event.DelayQueueInitHandler;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 延迟队列 配置
 * @author: 阿宽不是程序员
 **/
@EnableConfigurationProperties(DelayQueueProperties.class)
public class DelayQueueAutoConfig {
    
    @Bean
    public DelayQueueInitHandler delayQueueInitHandler(DelayQueueProperties delayQueueProperties, RedissonClient redissonClient){
        return new DelayQueueInitHandler(delayQueueProperties, redissonClient);
    }
   
    @Bean
    public DelayQueueBasePart delayQueueBasePart(RedissonClient redissonClient,DelayQueueProperties delayQueueProperties){
        return new DelayQueueBasePart(redissonClient,delayQueueProperties);
    }
  
    @Bean
    public DelayQueueContext delayQueueContext(DelayQueueBasePart delayQueueBasePart){
        return new DelayQueueContext(delayQueueBasePart);
    }
}
