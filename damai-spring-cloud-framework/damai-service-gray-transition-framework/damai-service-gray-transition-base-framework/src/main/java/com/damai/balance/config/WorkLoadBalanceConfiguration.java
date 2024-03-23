package com.damai.balance.config;


import com.damai.balance.ExtraZoneAvoidancePredicate;
import com.damai.balance.ExtraZoneAvoidanceRuleEnhance;
import com.damai.context.ContextHandler;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.context.annotation.Bean;

import static com.damai.constant.Constant.SERVER_GRAY;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: Ribbon负载均衡规则选择器
 * @author: 阿宽不是程序员
 **/
@AutoConfigureBefore(RibbonClientConfiguration.class)
public class WorkLoadBalanceConfiguration {
    
    @Value(SERVER_GRAY)
    private String serverGray;

    @RibbonClientName
    private String serviceId = "client";


    @Bean
    public IRule ribbonRule(PropertiesFactory propertiesFactory, IClientConfig config, ContextHandler contextHandler) {
        if (propertiesFactory.isSet(IRule.class, serviceId)) {
            return propertiesFactory.get(IRule.class, config, serviceId);
        }
        
        ExtraZoneAvoidanceRuleEnhance extraZoneAvoidanceRuleEnhance = new ExtraZoneAvoidanceRuleEnhance();
        extraZoneAvoidanceRuleEnhance.initWithNiwsConfig(config);
        
        ExtraZoneAvoidancePredicate cookPatchEnabledPredicate = extraZoneAvoidanceRuleEnhance.getExtraZoneAvoidancePredicate();
        cookPatchEnabledPredicate.setServerGray(serverGray);
        cookPatchEnabledPredicate.setContextHandler(contextHandler);
        return extraZoneAvoidanceRuleEnhance;
    }
}