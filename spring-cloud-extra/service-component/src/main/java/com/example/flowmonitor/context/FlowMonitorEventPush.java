package com.example.flowmonitor.context;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

/**
 * @program: 
 * @description:
 * @author: 星哥
 * @create: 2023-04-24
 **/
public class FlowMonitorEventPush implements ApplicationListener<ApplicationStartedEvent> {
    
    private ApplicationContext applicationContext;
    
    public FlowMonitorEventPush(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        applicationContext.publishEvent(new FlowMonitorEvent(this));
    }
}
