package com.example.runlistener;

import com.example.core.StringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-09-15
 **/
public class CustomEventPublishingRunListener implements SpringApplicationRunListener, Ordered {
    
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    
    private final SpringApplication application;
    
    private final String[] args;
    
    public CustomEventPublishingRunListener(SpringApplication application, String[] args){
        this.application = application;
        this.args = args;
    }
    @Override
    public int getOrder() {
        return 1;
    }
    
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        String applicationName = environment.getProperty(SPRING_APPLICATION_NAME);
        if (StringUtil.isNotEmpty(applicationName)) {
            System.setProperty("applicationName", applicationName);
        }
    }
}
