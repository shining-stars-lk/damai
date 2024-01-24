package com.example.delayqueuenew.event;

import cn.hutool.core.collection.CollectionUtil;
import com.example.delayqueuenew.config.DelayQueueProperties;
import com.example.delayqueuenew.context.DelayQueueContext;
import com.example.delayqueuenew.context.DelayQueuePart;
import com.example.delayqueuenew.core.ConsumerTask;
import com.example.delayqueuenew.core.DelayConsumerQueue;
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
    
    private final DelayQueueContext delayQueueContext;
    
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
                DelayConsumerQueue delayConsumerQueue = new DelayConsumerQueue(delayQueuePart, delayQueuePart.getConsumerTask().topic() + "-" + i);
                delayConsumerQueue.listenStart();
            }
        }
    }
    
    
}
