package com.example.kafka;

import com.alibaba.fastjson.JSON;
import com.example.entity.ApiData;
import com.example.service.ApiDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-15
 **/
@Slf4j
@AllArgsConstructor
public class ApiDataMessageConsumer {
    
    private ApiDataService apiDataService;
    
    @KafkaListener(topics = {"${kafka.consumer.topic:save_api_data}"},containerFactory = "kafkaListenerContainerFactory")
    public void consumerOrderMessage(ConsumerRecord consumerRecord){
        try {
            Optional.ofNullable(consumerRecord.value()).map(String::valueOf).ifPresent(value -> {
                log.info("consumerOrderMessage message:{}",value);
                ApiData apiData = JSON.parseObject(value, ApiData.class);
                apiDataService.save(apiData);
            });
        }catch (Exception e) {
            log.error("consumerApiDataMessage error",e);
        }
    }
}
