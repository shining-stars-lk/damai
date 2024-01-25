package com.example.service.delaysend;

import com.example.context.DelayQueueContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.service.constant.ProgramOrderConstant.DELAY_ORDER_CANCEL_TIME;
import static com.example.service.constant.ProgramOrderConstant.DELAY_ORDER_CANCEL_TIME_UNIT;
import static com.example.service.constant.ProgramOrderConstant.DELAY_ORDER_CANCEL_TOPIC;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-24
 **/
@Slf4j
@Component
public class DelayOrderCancelSend {
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    public void sendMessage(String message){
        try {
            delayQueueContext.sendMessage(DELAY_ORDER_CANCEL_TOPIC,message, DELAY_ORDER_CANCEL_TIME, DELAY_ORDER_CANCEL_TIME_UNIT);
        }catch (Exception e) {
            log.error("send message error message : {}",message,e);
        }
        
    }
}
