package com.extra.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: feign扩展插件配置类
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/

@Configuration(proxyBeanMethods = false)
public class ExtraFeignAutoConfiguration {
    
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor (){
        return new FeignRequestInterceptor();
    }
}
