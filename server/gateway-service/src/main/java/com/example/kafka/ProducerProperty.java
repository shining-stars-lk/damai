package com.example.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-14
 **/
@Data
@ConfigurationProperties(prefix = ProducerProperty.PREFIX)
public class ProducerProperty {
    
    public static final String PREFIX = "kafka.producer";
    
    private String servers;
    private int retries = 1;
    private String topic;

}
