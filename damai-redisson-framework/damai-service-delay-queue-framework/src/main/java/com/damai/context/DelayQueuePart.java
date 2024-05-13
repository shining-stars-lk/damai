package com.damai.context;

import com.damai.core.ConsumerTask;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 消息主题
 * @author: 阿星不是程序员
 **/
@Data
public class DelayQueuePart {
    
    private final DelayQueueBasePart delayQueueBasePart;
 
    private final ConsumerTask consumerTask;
    
    public DelayQueuePart(DelayQueueBasePart delayQueueBasePart, ConsumerTask consumerTask){
        this.delayQueueBasePart = delayQueueBasePart;
        this.consumerTask = consumerTask;
    }
}
