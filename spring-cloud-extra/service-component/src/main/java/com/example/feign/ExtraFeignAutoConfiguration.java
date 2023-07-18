package com.example.feign;

import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description: feign扩展插件配置类
 * @author: 星哥
 * @create: 2023-04-17
 **/

public class ExtraFeignAutoConfiguration {
    
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor (){
        return new FeignRequestInterceptor();
    }
}
