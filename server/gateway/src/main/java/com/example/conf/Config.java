package com.example.conf;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

/**
 * @program: gateway
 * @description:
 * @author: 星哥
 * @create: 2024-4-25
 **/
@Configuration
public class Config {
    
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer JacksonCustom(){
        return new JacksonCustom();
    }
}
