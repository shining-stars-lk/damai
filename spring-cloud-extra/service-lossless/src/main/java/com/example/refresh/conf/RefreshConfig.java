package com.example.refresh.conf;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.example.refresh.custom.NacosCustom;
import com.example.refresh.custom.RibbonCustom;
import com.example.refresh.custom.NacosAndRibbonCustom;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @description: 服务管理
 * @author: kuan
 * @create: 2023-06-08
 **/

@EnableConfigurationProperties
@AutoConfigureAfter({NacosAutoServiceRegistration.class})
public class RefreshConfig {

    @Bean
    public NacosCustom nacosCustom(NacosDiscoveryProperties discoveryProperties, NacosAutoServiceRegistration nacosAutoServiceRegistration){
        return new NacosCustom(discoveryProperties,nacosAutoServiceRegistration);
    }

    @Bean
    public RibbonCustom ribbonCustom(){
        return new RibbonCustom();
    }

    @Bean
    public NacosAndRibbonCustom nacosAndRibbonCustom(NacosCustom nacosCustom, RibbonCustom ribbonCustom){
        return new NacosAndRibbonCustom(nacosCustom, ribbonCustom);
    }

    @Bean
    public NacosLifecycle nacosLifecycle(NacosAndRibbonCustom nacosAndRibbonCustom, NacosDiscoveryProperties properties){
        return new NacosLifecycle(nacosAndRibbonCustom,properties);
    }
}
