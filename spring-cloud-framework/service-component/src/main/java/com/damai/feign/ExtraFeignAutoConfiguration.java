package com.damai.feign;

import org.springframework.context.annotation.Bean;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: feign扩展插件配置类
 * @author: 阿宽不是程序员
 **/

public class ExtraFeignAutoConfiguration {
    
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor (){
        return new FeignRequestInterceptor();
    }
}
