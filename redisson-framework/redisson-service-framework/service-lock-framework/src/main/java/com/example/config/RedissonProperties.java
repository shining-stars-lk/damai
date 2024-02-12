package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: redis-example
 * @description: 将yml的配置和此对象绑定
 * @author: 星哥
 * @create: 2023-05-28
 **/
@Data
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
public class RedissonProperties {

    public static final String PREFIX = "redisson";
    
    private String rbLoomFilterName = "user_register_rb_loom_filter";
    
    private Long expectedInsertions = 20000L;
    
    private Double falseProbability = 0.01D;
}
