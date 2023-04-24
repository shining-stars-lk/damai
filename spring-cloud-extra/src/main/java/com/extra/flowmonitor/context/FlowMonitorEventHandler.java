package com.extra.flowmonitor.context;

import com.extra.flowmonitor.hook.InitializingHookManager;
import com.extra.flowmonitor.writer.FlowMonitorWrite;
import org.springframework.context.ApplicationListener;

/**
 * @program: 
 * @description:
 * @author: lk
 * @create: 2023-04-24
 **/
public class FlowMonitorEventHandler implements ApplicationListener<FlowMonitorEvent> {

    @Override
    public void onApplicationEvent(final FlowMonitorEvent event) {
        FlowMonitorVirtualUriLoader.loadConsumerUris();
        FlowMonitorVirtualUriLoader.loadProviderUris();
        FlowMonitorWrite.initScheduleWriteData();
        InitializingHookManager.INSTANCE.boot();
    }
}
