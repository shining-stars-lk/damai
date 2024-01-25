package com.example.context;

import com.example.core.DelayConsumerQueue;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
public class DelayQueueConsumerCombine {
    
    public DelayQueueConsumerCombine(DelayQueuePart delayQueuePart){
        Integer isolationRegionCount = delayQueuePart.getDelayQueueProperties().getIsolationRegionCount();
        for(int i = 0; i < isolationRegionCount; i++) {
            DelayConsumerQueue delayConsumerQueue = new DelayConsumerQueue(delayQueuePart, delayQueuePart.getConsumerTask().topic() + "-" + i);
            delayConsumerQueue.listenStart();
        }
    }
}
