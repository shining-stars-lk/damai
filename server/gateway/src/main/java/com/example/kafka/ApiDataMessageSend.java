package com.example.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-29
 **/
@AllArgsConstructor
public class ApiDataMessageSend {
    
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private String topic;
    
    public void sendMessage(String message) {
        kafkaTemplate.send(topic,message);
    }
}
