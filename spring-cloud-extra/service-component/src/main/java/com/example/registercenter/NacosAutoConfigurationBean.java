package com.example.registercenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
public class NacosAutoConfigurationBean {
    
    public static String nacosDiscoveryAutoConfiguration = "com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration";
    
    public static String ribbonNacosAutoConfiguration = "com.alibaba.cloud.nacos.ribbon.RibbonNacosAutoConfiguration";
    
    public static String nacosDiscoveryEndpointAutoConfiguration = "com.alibaba.cloud.nacos.endpoint.NacosDiscoveryEndpointAutoConfiguration";
    
    public static String nacosServiceRegistryAutoConfiguration = "com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration";
    
    public static String nacosDiscoveryClientConfiguration = "com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientConfiguration";
    
    public static String nacosReactiveDiscoveryClientConfiguration = "com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClientConfiguration";
    
    public static String nacosConfigServerAutoConfiguration = "com.alibaba.cloud.nacos.discovery.configclient.NacosConfigServerAutoConfiguration";
    
    public static String nacosServiceAutoConfiguration = "com.alibaba.cloud.nacos.NacosServiceAutoConfiguration";
    
    public static String nacosDiscoveryClientConfigServiceBootstrapConfiguration = "com.alibaba.cloud.nacos.discovery.configclient.NacosDiscoveryClientConfigServiceBootstrapConfiguration";
    
    public static String nacosServiceConfig = "com.nacosrefresh.conf.NacosServiceConfig";
    
    public static Map<String,String> map = new HashMap<>(10);
    
    static {
        map.put(nacosDiscoveryAutoConfiguration,nacosDiscoveryAutoConfiguration);
        map.put(ribbonNacosAutoConfiguration,ribbonNacosAutoConfiguration);
        map.put(nacosDiscoveryEndpointAutoConfiguration,nacosDiscoveryEndpointAutoConfiguration);
        map.put(nacosServiceRegistryAutoConfiguration,nacosServiceRegistryAutoConfiguration);
        map.put(nacosDiscoveryClientConfiguration,nacosDiscoveryClientConfiguration);
        map.put(nacosReactiveDiscoveryClientConfiguration,nacosReactiveDiscoveryClientConfiguration);
        map.put(nacosConfigServerAutoConfiguration,nacosConfigServerAutoConfiguration);
        map.put(nacosServiceAutoConfiguration,nacosServiceAutoConfiguration);
        map.put(nacosDiscoveryClientConfigServiceBootstrapConfiguration,nacosDiscoveryClientConfigServiceBootstrapConfiguration);
        map.put(nacosServiceConfig,nacosServiceConfig);
    }
    
    public static Map<String,String> autoConfigurationBeanNameMap() {
        return map;
    }
}
