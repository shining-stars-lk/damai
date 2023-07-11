package com.example.feign;

import org.springframework.context.annotation.Bean;

/**
 * @program: feign扩展插件配置类
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/

public class ExtraFeignAutoConfiguration {
    
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor (){
        return new FeignRequestInterceptor();
    }
}
