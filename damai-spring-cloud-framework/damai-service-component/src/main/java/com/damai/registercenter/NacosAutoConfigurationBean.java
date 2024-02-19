package com.damai.registercenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: nacos排除
 * @author: 阿宽不是程序员
 **/
public class NacosAutoConfigurationBean {
    
    private static final String nacosDiscoveryAutoConfiguration = "com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration";
    
    private static final String ribbonNacosAutoConfiguration = "com.alibaba.cloud.nacos.ribbon.RibbonNacosAutoConfiguration";
    
    private static final String nacosDiscoveryEndpointAutoConfiguration = "com.alibaba.cloud.nacos.endpoint.NacosDiscoveryEndpointAutoConfiguration";
    
    private static final String nacosServiceRegistryAutoConfiguration = "com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration";
    
    private static final String nacosDiscoveryClientConfiguration = "com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientConfiguration";
    
    private static final String nacosReactiveDiscoveryClientConfiguration = "com.alibaba.cloud.nacos.discovery.reactive.NacosReactiveDiscoveryClientConfiguration";
    
    private static final String nacosConfigServerAutoConfiguration = "com.alibaba.cloud.nacos.discovery.configclient.NacosConfigServerAutoConfiguration";
    
    private static final String nacosServiceAutoConfiguration = "com.alibaba.cloud.nacos.NacosServiceAutoConfiguration";
    
    private static final String nacosDiscoveryClientConfigServiceBootstrapConfiguration = "com.alibaba.cloud.nacos.discovery.configclient.NacosDiscoveryClientConfigServiceBootstrapConfiguration";
    
    private static final String nacosServiceConfig = "com.nacosrefresh.conf.NacosServiceConfig";
    
    private static final Map<String,String> map = new HashMap<>(10);
    
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
