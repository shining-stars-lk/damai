package com.damai.kafka;

import com.alibaba.fastjson.JSON;
import com.damai.entity.ApiData;
import com.damai.service.ApiDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: kafka 消费
 * @author: 阿星不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class ApiDataMessageConsumer {
    
    private ApiDataService apiDataService;
    
    //@KafkaListener(topics = {"${kafka.consumer.topic:save_api_data}"},containerFactory = "kafkaListenerContainerFactory")
    public void consumerApiDataMessage(ConsumerRecord consumerRecord){
        try {
            Optional.ofNullable(consumerRecord.value()).map(String::valueOf).ifPresent(value -> {
                log.info("consumerApiDataMessage consumerRecord:{}",JSON.toJSONString(consumerRecord));
                ApiData apiData = JSON.parseObject(value, ApiData.class);
                apiDataService.saveApiData(apiData);
            });
        }catch (Exception e) {
            log.error("consumerApiDataMessage error",e);
        }
    }
}
