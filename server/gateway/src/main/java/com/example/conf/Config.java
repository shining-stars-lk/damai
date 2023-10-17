package com.example.conf;

import com.example.exception.GatewayDefaultExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

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
    
    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler(){
        return new GatewayDefaultExceptionHandler();
    }
    
    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration config = new CorsConfiguration();
        //设置是否允许cookie进行跨域
        config.setAllowCredentials(true);
        //允许跨域访问任何请求方式：post get put delete
        config.addAllowedMethod("*");
        //springboot2.4之前的使用
        config.addAllowedOrigin("*");
        //允许哪种请求来源
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
