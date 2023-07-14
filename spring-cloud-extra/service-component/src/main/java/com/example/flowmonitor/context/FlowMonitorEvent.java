package com.example.flowmonitor.context;

import org.springframework.context.ApplicationEvent;

/**
 * @program: 
 * @description:
 * @author: kuan
 * @create: 2023-04-24
 **/
public class FlowMonitorEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    public FlowMonitorEvent(final Object source) {
        super(source);
    }
}
