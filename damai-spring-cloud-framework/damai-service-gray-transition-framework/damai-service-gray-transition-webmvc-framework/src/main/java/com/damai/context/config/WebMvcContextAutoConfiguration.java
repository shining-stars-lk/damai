package com.damai.context.config;

import com.damai.context.ContextHandler;
import com.damai.context.impl.WebMvcContextHandler;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: WebMvc配置
 * @author: 阿星不是程序员
 **/
public class WebMvcContextAutoConfiguration {
    
    @Bean
    public ContextHandler webMvcContext(){
        return new WebMvcContextHandler();
    }
}
