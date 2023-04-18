package com.extra.ribbon;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ExtraRibbonProperties.class)
public class ExtraRibbonAutoConfiguration {
    
    @Bean
    public DiscoveryEnabledRule discoveryEnabledRule(ExtraRibbonProperties extraRibbonProperties){
        return new DiscoveryEnabledRule(extraRibbonProperties);
    }
}
