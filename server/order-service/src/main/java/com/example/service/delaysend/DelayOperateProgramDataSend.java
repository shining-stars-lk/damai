package com.example.service.delaysend;

import com.example.context.DelayQueueContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.service.constant.OrderConstant.DELAY_OPERATE_PROGRAM_DATA_TIME;
import static com.example.service.constant.OrderConstant.DELAY_OPERATE_PROGRAM_DATA_TIME_UNIT;
import static com.example.service.constant.OrderConstant.DELAY_OPERATE_PROGRAM_DATA_TOPIC;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-24
 **/
@Slf4j
@Component
public class DelayOperateProgramDataSend {
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    public void sendMessage(String message){
        try {
            delayQueueContext.sendMessage(DELAY_OPERATE_PROGRAM_DATA_TOPIC,message, DELAY_OPERATE_PROGRAM_DATA_TIME, DELAY_OPERATE_PROGRAM_DATA_TIME_UNIT);
        }catch (Exception e) {
            log.error("send message error message : {}",message,e);
        }
        
    }
}
