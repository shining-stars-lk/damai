package com.damai.balance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;

import static com.damai.constant.Constant.SERVER_GRAY;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 灰度版本选择相关配置
 * @author: 阿宽不是程序员
 **/

@AutoConfigureBefore(value = {RibbonClientConfiguration.class})
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
public class ExtraRibbonAutoConfiguration {
    
    @Value(SERVER_GRAY)
    public String serverGray;
    
    @Bean
    public CustomEnabledRule discoveryEnabledRule(){
        return new CustomEnabledRule(serverGray);
    }
}
