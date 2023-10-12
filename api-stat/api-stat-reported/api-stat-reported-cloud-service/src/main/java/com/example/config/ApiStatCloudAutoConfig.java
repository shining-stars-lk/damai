package com.example.config;

import com.example.common.ApiStatCommon;
import com.example.common.impl.ApiStatCloudServiceCommon;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@Data
@EnableConfigurationProperties(ApiStatProperties.class)
@ConditionalOnProperty(value = "api-stat.enable",havingValue = "true")
@AutoConfigureBefore(ApiStatAutoConfig.class)
public class ApiStatCloudAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ApiStatCommon ApiStatSingleServiceCommon(){
        return new ApiStatCloudServiceCommon();
    }
}
