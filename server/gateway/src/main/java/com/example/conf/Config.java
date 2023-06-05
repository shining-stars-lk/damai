package com.example.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @program: gateway
 * @description:
 * @author: lk
 * @create: 2024-4-25
 **/
@Configuration
public class Config {
    
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
