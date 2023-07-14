package com.example.filter;

import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-05-09
 **/

public class FilterAutoConfiguration {
    
    @Bean
    public BaseParameterFilter baseParameterFilter(){
        return new BaseParameterFilter();
    }
}
