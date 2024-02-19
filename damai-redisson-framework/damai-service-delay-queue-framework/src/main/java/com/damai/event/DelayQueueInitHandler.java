package com.damai.event;

import cn.hutool.core.collection.CollectionUtil;
import com.damai.config.DelayQueueProperties;
import com.damai.context.DelayQueuePart;
import com.damai.core.ConsumerTask;
import com.damai.core.DelayConsumerQueue;
import com.damai.core.SpringUtil;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;

/**
 * DelayQueueInitHandler 类用于处理应用程序启动事件。
 */
@AllArgsConstructor
public class DelayQueueInitHandler implements ApplicationListener<ApplicationStartedEvent> {
    
    private final DelayQueueProperties delayQueueProperties;
    
    private final RedissonClient redissonClient;
    
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, ConsumerTask> consumerTaskMap = event.getApplicationContext().getBeansOfType(ConsumerTask.class);
        if (CollectionUtil.isEmpty(consumerTaskMap)) {
            return;
        }
        for (ConsumerTask consumerTask : consumerTaskMap.values()) {
            DelayQueuePart delayQueuePart = new DelayQueuePart(redissonClient,delayQueueProperties,consumerTask);
            Integer isolationRegionCount = delayQueuePart.getDelayQueueProperties().getIsolationRegionCount();
            for(int i = 0; i < isolationRegionCount; i++) {
                DelayConsumerQueue delayConsumerQueue = new DelayConsumerQueue(delayQueuePart, 
                        SpringUtil.getPrefixDistinctionName() + "-" + delayQueuePart.getConsumerTask().topic() + "-" + i);
                delayConsumerQueue.listenStart();
            }
        }
    }
    
    
}
