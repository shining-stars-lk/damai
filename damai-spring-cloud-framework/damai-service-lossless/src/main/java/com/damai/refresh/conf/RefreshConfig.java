package com.damai.refresh.conf;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.damai.refresh.custom.NacosAndRibbonCustom;
import com.damai.refresh.custom.NacosCustom;
import com.damai.refresh.custom.RibbonCustom;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 服务切换流量无损失配置
 * @author: 阿宽不是程序员
 **/


@AutoConfigureAfter({NacosAutoServiceRegistration.class})
public class RefreshConfig {

    @Bean
    public NacosCustom nacosCustom(NacosDiscoveryProperties discoveryProperties, 
                                   NacosAutoServiceRegistration nacosAutoServiceRegistration,
                                   NacosServiceManager nacosServiceManager){
        return new NacosCustom(discoveryProperties,nacosAutoServiceRegistration,nacosServiceManager);
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
    public NacosLifecycle nacosLifecycle(RibbonCustom ribbonCustom, NacosDiscoveryProperties properties){
        return new NacosLifecycle(ribbonCustom,properties);
    }
}
