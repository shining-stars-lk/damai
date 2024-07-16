package com.damai.conf;

import com.damai.exception.GatewayDefaultExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 通用配置
 * @author: 阿星不是程序员
 **/
//@EnableConfigurationProperties(ServerProperties.class)
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
    
    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return ServerCodecConfigurer.create();
    }

//    @Bean
//    public GatewaySentinelConfiguration gatewaySentinelConfiguration(
//            ObjectProvider<List<ViewResolver>> viewResolversProvider,
//            ServerCodecConfigurer serverCodecConfigurer){
//        return new GatewaySentinelConfiguration(viewResolversProvider,serverCodecConfigurer);
//    }
}
