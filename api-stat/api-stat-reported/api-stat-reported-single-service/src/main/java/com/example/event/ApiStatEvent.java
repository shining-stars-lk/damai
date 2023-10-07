package com.example.event;

import org.springframework.context.ApplicationEvent;

/**
 * @program: 
 * @description:
 * @author:
 * @create: 2023-10-03
 **/
public class ApiStatEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    public ApiStatEvent(final Object source) {
        super(source);
    }
}