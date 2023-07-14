package com.example.listenerevent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-09
 **/
@Component
public class TestEventHandler implements ApplicationListener<TestEvent> {
    @Override
    public void onApplicationEvent(final TestEvent testEvent) {
        
        System.out.println(testEvent.getSource() + "监听开始执行");
    }
}
