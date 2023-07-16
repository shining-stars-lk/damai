package com.example.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-29
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
