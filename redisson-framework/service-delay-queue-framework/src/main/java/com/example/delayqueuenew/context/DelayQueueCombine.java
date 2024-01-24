package com.example.delayqueuenew.context;

import com.example.delayqueuenew.core.DelayQueue;
import com.example.delayqueuenew.core.IsolationRegionSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
public class DelayQueueCombine {
    
    private final IsolationRegionSelector isolationRegionSelector;
    
    private final List<DelayQueue> delayQueueList = new ArrayList<>();
    
    public DelayQueueCombine(DelayQueuePart delayQueuePart){
        Integer isolationRegionCount = delayQueuePart.getDelayQueueProperties().getIsolationRegionCount();
        isolationRegionSelector =new IsolationRegionSelector(isolationRegionCount);
        for(int i = 0; i < isolationRegionCount; i++) {
            delayQueueList.add(new DelayQueue(delayQueuePart,delayQueuePart.getConsumerTask().topic() + "-" + i));
        }
    }
    
    public void offer(String content,long delayTime, TimeUnit timeUnit){
        int index = isolationRegionSelector.getIndex();
        delayQueueList.get(index).offer(content, delayTime, timeUnit);
    }
}
