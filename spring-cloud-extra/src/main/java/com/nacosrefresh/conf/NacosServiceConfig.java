package com.nacosrefresh.conf;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.nacosrefresh.handle.NacosHandle;
import com.nacosrefresh.handle.RibbonHandle;
import com.nacosrefresh.handle.ServiceHandle;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: bean管理
 * @author: k
 * @create: 2022-06-08
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@AutoConfigureAfter({NacosAutoServiceRegistration.class})
public class NacosServiceConfig {

    @Bean
    public NacosHandle nacosHandle(NacosDiscoveryProperties discoveryProperties, NacosAutoServiceRegistration nacosAutoServiceRegistration){
        return new NacosHandle(discoveryProperties,nacosAutoServiceRegistration);
    }

    @Bean
    public RibbonHandle ribbonHandle(){
        return new RibbonHandle();
    }

    @Bean
    public ServiceHandle serviceHandle(NacosHandle nacosHandle, RibbonHandle ribbonHandle){
        return new ServiceHandle(nacosHandle, ribbonHandle);
    }

    @Bean
    public NacosServiceRefresher nacosServiceRefresher(ServiceHandle serviceHandle, NacosDiscoveryProperties properties){
        return new NacosServiceRefresher(serviceHandle,properties);
    }
}
