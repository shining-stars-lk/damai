package com.damai.service.delaysend;

import com.damai.context.DelayQueueContext;
import com.damai.core.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.damai.service.constant.OrderConstant.DELAY_OPERATE_PROGRAM_DATA_TIME;
import static com.damai.service.constant.OrderConstant.DELAY_OPERATE_PROGRAM_DATA_TIME_UNIT;
import static com.damai.service.constant.OrderConstant.DELAY_OPERATE_PROGRAM_DATA_TOPIC;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单支付成功后 更新相关数据
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class DelayOperateProgramDataSend {
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    public void sendMessage(String message){
        try {
            delayQueueContext.sendMessage(SpringUtil.getPrefixDistinctionName() + "-" + DELAY_OPERATE_PROGRAM_DATA_TOPIC,
                    message, DELAY_OPERATE_PROGRAM_DATA_TIME, DELAY_OPERATE_PROGRAM_DATA_TIME_UNIT);
        }catch (Exception e) {
            log.error("send message error message : {}",message,e);
        }
        
    }
}
