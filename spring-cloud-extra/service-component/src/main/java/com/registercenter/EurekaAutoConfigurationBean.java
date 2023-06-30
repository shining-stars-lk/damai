package com.registercenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
public class EurekaAutoConfigurationBean {
    
    public static String DiscoveryClientOptionalArgsConfiguration = "org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration";
    
    public static String EurekaClientAutoConfiguration = "org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration";
    
    public static String RibbonEurekaAutoConfiguration = "org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration";
    
    public static String EurekaDiscoveryClientConfiguration = "org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration";
    
    public static String EurekaReactiveDiscoveryClientConfiguration = "org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClientConfiguration";
    
    public static String LoadBalancerEurekaAutoConfiguration = "org.springframework.cloud.netflix.eureka.loadbalancer.LoadBalancerEurekaAutoConfiguration";
    
    public static Map<String,String> map = new HashMap<>(10);
    
    static {
        map.put(DiscoveryClientOptionalArgsConfiguration,DiscoveryClientOptionalArgsConfiguration);
        map.put(EurekaClientAutoConfiguration,EurekaClientAutoConfiguration);
        map.put(RibbonEurekaAutoConfiguration,RibbonEurekaAutoConfiguration);
        map.put(EurekaDiscoveryClientConfiguration,EurekaDiscoveryClientConfiguration);
        map.put(EurekaReactiveDiscoveryClientConfiguration,EurekaReactiveDiscoveryClientConfiguration);
        map.put(LoadBalancerEurekaAutoConfiguration,LoadBalancerEurekaAutoConfiguration);
    }
    
    public static Map<String,String> autoConfigurationBeanNameMap() {
        return map;
    }
}
