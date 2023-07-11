package com.example.balance;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/

@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ExtraRibbonProperties.class)
public class ExtraRibbonAutoConfiguration {
    
    @Bean
    public CustomEnabledRule discoveryEnabledRule(ExtraRibbonProperties extraRibbonProperties){
        return new CustomEnabledRule(extraRibbonProperties);
    }
}
