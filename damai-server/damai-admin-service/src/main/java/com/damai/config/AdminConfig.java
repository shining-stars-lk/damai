package com.damai.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-09-29
 **/

public class AdminConfig {
    
    @Primary
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomEnhance(){
        return new JacksonCustomEnhance();
    }
}
