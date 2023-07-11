package com.example.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * 
 * @description:
 * @author: lk
 * @create: 2022-10-31
 **/
@Configuration
public class SwaggerConfig {
    
    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder().build();
    }
    
    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder().showExtensions(true).build();
    }
    
}
