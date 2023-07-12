package com.example.conf.swagger;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-03
 **/

public class SwaggerConfig {
    
    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder().build();
    }
    
    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder().showExtensions(true).build();
    }
    
    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerProvider(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, GatewayProperties gatewayProperties){
        return new SwaggerProvider(discoveryClient, loadBalancerClient, gatewayProperties);
    }
    
}
