package com.example.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.Order;
import com.example.entity.ProductOrder;
import com.example.mapper.OrderMapper;
import com.example.mapper.ProductOrderMapper;
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

    private OrderMapper orderMapper;
    
    private ProductOrderMapper productOrderMapper;
    
    @KafkaListener(topics = {"${kafka.consumer.topic:order}"},containerFactory = "kafkaListenerContainerFactory")
    public void consumerOrderMessage(ConsumerRecord consumerRecord){
        Object key = consumerRecord.key();
        String value = (String)consumerRecord.value();
        JSONObject jsonObject = JSON.parseObject(value);
        Order order = jsonObject.getObject("order", Order.class);
        JSONArray productOrderJSONArray = jsonObject.getJSONArray("productOrderList");
        List<ProductOrder> productOrderList = productOrderJSONArray.toJavaList(ProductOrder.class);
        
        try {
            orderMapper.insert(order);
            for (ProductOrder productOrder : productOrderList) {
                productOrderMapper.insert(productOrder);
            }
        }catch (Exception e) {
            log.error("consumerOrderMessage error",e);
        }
    }
}
