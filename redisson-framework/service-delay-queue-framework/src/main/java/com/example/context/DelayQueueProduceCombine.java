package com.example.context;

import com.example.core.DelayProduceQueue;
import com.example.core.IsolationRegionSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-24
 **/
public class DelayQueueProduceCombine {
    
    private final IsolationRegionSelector isolationRegionSelector;
    
    private final List<DelayProduceQueue> delayProduceQueueList = new ArrayList<>();
    
    public DelayQueueProduceCombine(DelayQueueBasePart delayQueueBasePart,String topic){
        Integer isolationRegionCount = delayQueueBasePart.getDelayQueueProperties().getIsolationRegionCount();
        isolationRegionSelector =new IsolationRegionSelector(isolationRegionCount);
        for(int i = 0; i < isolationRegionCount; i++) {
            delayProduceQueueList.add(new DelayProduceQueue(delayQueueBasePart.getRedissonClient(),topic + "-" + i));
        }
    }
    
    public void offer(String content,long delayTime, TimeUnit timeUnit){
        int index = isolationRegionSelector.getIndex();
        delayProduceQueueList.get(index).offer(content, delayTime, timeUnit);
    }
}
