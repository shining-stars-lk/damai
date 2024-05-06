package com.damai.service.test2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = RedisStreamConfigProperties.PREFIX)
public class RedisStreamConfigProperties {
    
    public static final String PREFIX = "spring.redis.stream";
    
    private String streamName;
    
    private String consumerGroup;
    
    private String consumerName;
}

