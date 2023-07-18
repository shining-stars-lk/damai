package com.example.stat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-13
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
        // 创建请求数自定义指标
        // 计数器可以用于记录只会增加不会减少的指标类型，比如记录应用请求的总量(api_request_total)，
        // 一般而言，Counter类型的metrics指标在命名中，我们使用_total结束。
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
