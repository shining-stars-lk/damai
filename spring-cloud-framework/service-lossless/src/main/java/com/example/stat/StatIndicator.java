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
