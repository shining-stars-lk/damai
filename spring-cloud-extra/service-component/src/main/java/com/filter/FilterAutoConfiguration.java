package com.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-05-09
 **/
@Configuration(proxyBeanMethods = false)
public class FilterAutoConfiguration {
    
    @Bean
    public BaseParameterFilter baseParameterFilter(){
        return new BaseParameterFilter();
    }
}
