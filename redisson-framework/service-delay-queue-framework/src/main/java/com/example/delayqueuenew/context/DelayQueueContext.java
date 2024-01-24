package com.example.delayqueuenew.context;

import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
public class DelayQueueContext {
    
    private final Map<String, DelayQueueCombine> delayQueueCombineMap = new ConcurrentHashMap<>();
    
    public void putTask(String topic,DelayQueueCombine delayQueueCombine){
        delayQueueCombineMap.put(topic,delayQueueCombine);
    }
    
    public void sendMessage(String topic,String content,long delayTime, TimeUnit timeUnit) {
        DelayQueueCombine delayQueueCombine = Optional.ofNullable(delayQueueCombineMap.get(topic))
                .orElseThrow(() -> new CookFrameException(BaseCode.DELAY_QUEUE_CLIENT_NOT_EXIST));
        delayQueueCombine.offer(content, delayTime, timeUnit);
    }
}
