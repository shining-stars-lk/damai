package com.damai.stat;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 统计器配置
 * @author: 阿宽不是程序员
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
