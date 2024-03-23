package com.damai.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 数据发送
 * @author: 阿宽不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class ApiDataMessageSend {
    
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private String topic;
    
    public void sendMessage(String message) {
        log.info("sendMessage message : {}", message);
        kafkaTemplate.send(topic,message);
    }
}
