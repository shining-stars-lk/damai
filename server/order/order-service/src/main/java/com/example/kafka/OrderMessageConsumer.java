package com.example.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.ProductOrder;
import com.example.entity.PsOrder;
import com.example.service.OrderService;
import com.tool.delayqueue.Producer;
import com.tool.servicelock.redisson.RedissonProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-15
 **/
@Slf4j
@AllArgsConstructor
public class OrderMessageConsumer {

    private OrderService orderService;
    
    private Producer producer;
    
    private RedissonProperties redissonProperties;
    
    @KafkaListener(topics = {"${kafka.consumer.topic:order}"},containerFactory = "kafkaListenerContainerFactory")
    public void consumerOrderMessage(ConsumerRecord consumerRecord){
        try {
            Object key = consumerRecord.key();
            String value = (String)consumerRecord.value();
            JSONObject jsonObject = JSON.parseObject(value);
            PsOrder psOrder = jsonObject.getObject("order", PsOrder.class);
            JSONArray productOrderJSONArray = jsonObject.getJSONArray("productOrderList");
            List<ProductOrder> productOrderList = productOrderJSONArray.toJavaList(ProductOrder.class);
            orderService.saveOrderAndProductOrder(psOrder,productOrderList);
            //延迟队列发送消息
            producer.produceMessage(redissonProperties.getProduceTopic(),psOrder.getId(),redissonProperties.getDelayTime(),redissonProperties.getDelayTimeUnit());
        }catch (Exception e) {
            log.error("consumerOrderMessage error",e);
        }
    }
}
