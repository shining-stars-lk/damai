package com.example.service.delayconsumer;

import com.alibaba.fastjson.JSON;
import com.example.core.StringUtil;
import com.example.delayqueuenew.core.ConsumerTask;
import com.example.dto.DelayOrderCancelDto;
import com.example.dto.OrderCancelDto;
import com.example.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.service.constant.OrderConstant.DELAY_ORDER_CANCEL_TOPIC;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-21
 **/
@Slf4j
@Component
public class DelayOrderCancelConsumer implements ConsumerTask {
    
    @Autowired
    private OrderService orderService;
    
    @Override
    public void execute(String content) {
        log.info("延迟订单取消消息进行消费 content : {}", content);
        if (StringUtil.isEmpty(content)) {
            log.error("延迟队列消息不存在");
            return;
        }
        DelayOrderCancelDto delayOrderCancelDto = JSON.parseObject(content, DelayOrderCancelDto.class);
        
        //取消订单
        OrderCancelDto orderCancelDto = new OrderCancelDto();
        orderCancelDto.setOrderId(delayOrderCancelDto.getOrderId());
        boolean cancel = orderService.cancel(orderCancelDto);
        if (cancel) {
            log.info("延迟订单取消成功 orderCancelDto : {}",content);
        }else {
            log.error("延迟订单取消失败 orderCancelDto : {}",content);
        }
    }
    
    @Override
    public String topic() {
        return DELAY_ORDER_CANCEL_TOPIC;
    }
}
