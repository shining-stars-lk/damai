package com.damai.stat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 统计器
 * @author: 阿宽不是程序员
 **/

public class StatIndicator implements EnvironmentAware {
    
    private static final String REGISTRY_NAME = "api_request_total";
    
    private Environment environment;
    
    private Counter apiRequestCounter;
    
    private final MeterRegistry registry;
    
    public StatIndicator(MeterRegistry registry) {
        this.registry = registry;
    }
    
    @PostConstruct
    public void init(){
        apiRequestCounter = registry.counter(REGISTRY_NAME, "application", environment.getProperty("spring.application.name"));
    }
    
    public void increment(){
        apiRequestCounter.increment();
    }
    
    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }
}
