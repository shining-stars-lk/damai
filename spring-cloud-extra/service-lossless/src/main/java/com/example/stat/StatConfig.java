package com.example.stat;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-07-13
 **/
public class StatConfig {
    
    @Bean
    public StatIndicator statIndicator(MeterRegistry registry){
        return new StatIndicator(registry);
    }
    
    @Bean
    public StatFilter statFilter(StatIndicator statIndicator){
        return new StatFilter(statIndicator);
    }
}
