package com.example.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-11-27
 **/
public class JsonMapperAutoConfig {
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer JacksonCustom(){
        return new JacksonCustom();
    }
}
