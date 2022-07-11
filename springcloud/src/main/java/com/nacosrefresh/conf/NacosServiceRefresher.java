package com.nacosrefresh.conf;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.nacosrefresh.handle.ServiceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import java.util.concurrent.atomic.AtomicBoolean;


public class NacosServiceRefresher implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRefresher.class);

    private final AtomicBoolean running = new AtomicBoolean(false);

    private ServiceHandle serviceHandle;

    private NacosDiscoveryProperties properties;

    public NacosServiceRefresher(ServiceHandle serviceHandle, NacosDiscoveryProperties properties){
        this.serviceHandle = serviceHandle;
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
                        serviceHandle.updateNacosAndRibbonCache();
                        logger.warn("updateNacosAndRibbonCache completed ...");
                    },"service-refresher-thread").start();
                });
            }catch (Exception e) {
                logger.error("ServiceRefresher subscribe failed, properties:{}", properties, e);
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
                        serviceHandle.updateNacosAndRibbonCache();
                        logger.warn("updateNacosAndRibbonCache completed ...");
                    },"service-refresher-thread").start();
                });
            }catch (Exception e) {
                logger.error("ServiceRefresher unsubscribe failed, properties:{}", properties, e);
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
