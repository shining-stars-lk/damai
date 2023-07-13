package com.example.refresh.conf;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.example.refresh.handle.NacosAndRibbonCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class NacosLifecycle implements SmartLifecycle {
    
    private final AtomicBoolean running = new AtomicBoolean(false);

    private NacosAndRibbonCustom nacosAndRibbonCustom;

    private NacosDiscoveryProperties properties;

    public NacosLifecycle(NacosAndRibbonCustom nacosAndRibbonCustom, NacosDiscoveryProperties properties){
        this.nacosAndRibbonCustom = nacosAndRibbonCustom;
        this.properties = properties;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public void start(){
        if (this.running.compareAndSet(false, true)) {
            try {
                //开启订阅机制
                NamingService naming = NamingFactory.createNamingService(properties.getNacosProperties());
                naming.subscribe(properties.getService(),event -> {
                    new Thread(() -> {
                        //收到服务端的响应后，进行刷新nacos和ribbon列表
                        nacosAndRibbonCustom.refreshNacosAndRibbonCache();
                        log.warn("updateNacosAndRibbonCache completed ...");
                    },"service-refresher-thread").start();
                });
            }catch (Exception e) {
                log.error("ServiceRefresher subscribe failed, properties:{}", properties, e);
            }
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            try {
                NamingService naming = NamingFactory.createNamingService(properties.getNacosProperties());
                naming.unsubscribe(properties.getService(),event -> {
                    new Thread(() -> {
                        nacosAndRibbonCustom.refreshNacosAndRibbonCache();
                        log.warn("updateNacosAndRibbonCache completed ...");
                    },"service-refresher-thread").start();
                });
            }catch (Exception e) {
                log.error("ServiceRefresher unsubscribe failed, properties:{}", properties, e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public int getPhase() {
        return 1;
    }
}
