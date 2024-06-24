package com.damai.service.delaysend;

import com.damai.context.DelayQueueContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 延迟订单发送
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class DelayOrderCancelSend {
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    public void sendMessage(String message){
//        try {
//            log.info("延迟订单取消消息进行发送 消息体 : {}",message);
//            delayQueueContext.sendMessage(SpringUtil.getPrefixDistinctionName() + "-" + DELAY_ORDER_CANCEL_TOPIC,
//                    message, DELAY_ORDER_CANCEL_TIME, DELAY_ORDER_CANCEL_TIME_UNIT);
//        }catch (Exception e) {
//            log.error("send message error message : {}",message,e);
//        }
        
    }
}
