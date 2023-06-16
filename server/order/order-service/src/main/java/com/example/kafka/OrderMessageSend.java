package com.example.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Component
@AllArgsConstructor
public class OrderMessageSend {
    
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private String topic;
    
    public void sendMessage(String message) {
        kafkaTemplate.send(topic,message);
    }
}
