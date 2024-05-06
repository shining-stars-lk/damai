package com.damai.service.test2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redisstream")
public class RedisStreamConfig {
    static final String DCIR = "test_stream";
    private String dcirgroup;
    private String dcirconsumer;
}

