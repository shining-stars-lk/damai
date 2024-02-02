package com.example.config;

import com.example.toolkit.WorkAndDataCenterIdHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-02
 **/
public class IdGeneratorAutoConfig {
    
    @Bean
    public WorkAndDataCenterIdHandler workAndDataCenterIdHandler(StringRedisTemplate stringRedisTemplate){
        return new WorkAndDataCenterIdHandler(stringRedisTemplate);
    }
}
