package com.example.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-14
 **/
@Data
@ConfigurationProperties(prefix = ConsumerProperty.PREFIX)
public class ConsumerProperty {
    
    public static final String PREFIX = "kafka.consumer";
    
    private String servers;

    private boolean autoCommit;
    
    private String autoCommitIntervalMs;
    
    private String autoOffsetReset;
    
    private String groupId;
    
    private String topic;
}
