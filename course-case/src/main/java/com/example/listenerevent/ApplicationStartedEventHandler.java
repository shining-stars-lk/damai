package com.example.listenerevent;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-09
 **/
@Component
public class ApplicationStartedEventHandler implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        System.out.println("ApplicationStartedEvent事件监听执行");
    }
}
