package com.example.stat;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
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
