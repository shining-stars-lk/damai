package com.example.listenerevent;

import org.springframework.context.ApplicationEvent;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-09
 **/
public class TestEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    public TestEvent(final String source) {
        super(source);
    }
}
