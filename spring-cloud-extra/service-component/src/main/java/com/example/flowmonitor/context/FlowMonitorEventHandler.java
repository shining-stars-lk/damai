package com.example.flowmonitor.context;

import com.example.flowmonitor.hook.InitializingHookManager;
import com.example.flowmonitor.writer.FlowMonitorWrite;
import org.springframework.context.ApplicationListener;

/**
 * @program: 
 * @description:
 * @author: kuan
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
