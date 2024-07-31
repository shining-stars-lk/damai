package com.damai.conf;

import com.damai.exception.GatewayDefaultExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 通用配置
 * @author: 阿星不是程序员
 **/
public class Config implements WebFluxConfigurer {
    
    private final AtomicInteger threadCount = new AtomicInteger(1);
    
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
    
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600L);
    }
    
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors()+10,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> new Thread(
                        Thread.currentThread().getThreadGroup(), r,
                        "listen-start-thread-" + threadCount.getAndIncrement()));
    }
}
