package com.damai.registercenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: eureka排除
 * @author: 阿星不是程序员
 **/
public class EurekaAutoConfigurationBean {
    
    private static final String DISCOVERY_CLIENT_OPTIONAL_ARGS_CONFIGURATION = "org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration";
    
    private static final String EUREKA_CLIENT_AUTO_CONFIGURATION = "org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration";
    
    private static final String RIBBON_EUREKA_AUTO_CONFIGURATION = "org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration";
    
    private static final String EUREKA_DISCOVERY_CLIENT_CONFIGURATION = "org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration";
    
    private static final String EUREKA_REACTIVE_DISCOVERY_CLIENT_CONFIGURATION = "org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClientConfiguration";
    
    private static final String LOAD_BALANCER_EUREKA_AUTO_CONFIGURATION = "org.springframework.cloud.netflix.eureka.loadbalancer.LoadBalancerEurekaAutoConfiguration";
    
    private static final Map<String,String> MAP = new HashMap<>(10);
    
    static {
        MAP.put(DISCOVERY_CLIENT_OPTIONAL_ARGS_CONFIGURATION, DISCOVERY_CLIENT_OPTIONAL_ARGS_CONFIGURATION);
        MAP.put(EUREKA_CLIENT_AUTO_CONFIGURATION, EUREKA_CLIENT_AUTO_CONFIGURATION);
        MAP.put(RIBBON_EUREKA_AUTO_CONFIGURATION, RIBBON_EUREKA_AUTO_CONFIGURATION);
        MAP.put(EUREKA_DISCOVERY_CLIENT_CONFIGURATION, EUREKA_DISCOVERY_CLIENT_CONFIGURATION);
        MAP.put(EUREKA_REACTIVE_DISCOVERY_CLIENT_CONFIGURATION, EUREKA_REACTIVE_DISCOVERY_CLIENT_CONFIGURATION);
        MAP.put(LOAD_BALANCER_EUREKA_AUTO_CONFIGURATION, LOAD_BALANCER_EUREKA_AUTO_CONFIGURATION);
    }
    
    public static Map<String,String> autoConfigurationBeanNameMap() {
        return MAP;
    }
}
