package com.damai.service.kafka;

import com.alibaba.fastjson.JSON;
import com.damai.dto.OrderCreateDto;
import com.damai.service.OrderService;
import com.damai.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: kafka 创建订单 消费
 * @author: 阿星不是程序员
 **/
@Slf4j
@AllArgsConstructor
@Component
public class CreateOrderConsumer {
    
    @Autowired
    private OrderService orderService;
    
    @KafkaListener(topics = {"${spring.kafka.topic:create_order}"})
    public void consumerOrderMessage(ConsumerRecord<String,String> consumerRecord){
        try {
            Optional.ofNullable(consumerRecord.value()).map(String::valueOf).ifPresent(value -> {
                log.info("consumerOrderMessage message start message: {}",value);
                OrderCreateDto orderCreateDto = JSON.parseObject(value, OrderCreateDto.class);
                
                long createOrderTimeTimestamp = DateUtils.getDateTimeStampNo(orderCreateDto.getCreateOrderTime());
                
                long currentTimeTimestamp = System.currentTimeMillis();
                
                String orderNumber = orderService.createByMq(orderCreateDto);
                log.info("consumerOrderMessage message end orderNumber: {}",orderNumber);
            });
        }catch (Exception e) {
            log.error("consumerOrderMessage error",e);
        }
    }
}
