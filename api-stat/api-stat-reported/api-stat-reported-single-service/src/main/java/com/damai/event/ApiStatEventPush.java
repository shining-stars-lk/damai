package com.damai.event;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @program: 
 * @description:
 * @author: 星哥
 * @create: 2023-04-24
 **/

public class ApiStatEventPush implements ApplicationListener<ApplicationStartedEvent> {
    
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        event.getApplicationContext().publishEvent(new ApiStatEvent(this));
    }
}
