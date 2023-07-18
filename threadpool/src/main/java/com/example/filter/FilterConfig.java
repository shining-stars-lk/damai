package com.example.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-14
 **/
public class FilterConfig {

    @Bean
    public OncePerRequestFilter requestContextFilter(){
        return new RequestContextFilter();
    }
}
