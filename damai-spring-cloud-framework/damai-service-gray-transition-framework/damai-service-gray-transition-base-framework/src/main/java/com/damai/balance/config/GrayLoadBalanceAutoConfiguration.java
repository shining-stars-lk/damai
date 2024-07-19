package com.damai.balance.config;

import com.damai.context.ContextHandler;
import com.damai.enhance.config.EnhanceLoadBalancerClientConfiguration;
import com.damai.enhance.config.EnhanceLoadBalancerClientConfiguration.BlockingSupportConfiguration;
import com.damai.enhance.config.EnhanceLoadBalancerClientConfiguration.ReactiveSupportConfiguration;
import com.damai.filter.AbstractServerFilter;
import com.damai.filter.impl.ServerGrayFilter;
import com.damai.fiterbalance.DefaultFilterLoadBalance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 灰度版本选择相关配置
 * @author: 阿星不是程序员
 **/
@LoadBalancerClients(defaultConfiguration = {EnhanceLoadBalancerClientConfiguration.class, ReactiveSupportConfiguration.class, BlockingSupportConfiguration.class})
public class GrayLoadBalanceAutoConfiguration {
    
    @Bean
    public DefaultFilterLoadBalance defaultFilterLoadBalance(List<AbstractServerFilter> strategyEnabledFilterList){
        return new DefaultFilterLoadBalance(strategyEnabledFilterList);
    }
    
    @Bean
    public AbstractServerFilter serverGrayFilter(ContextHandler contextHandler) {
        return new ServerGrayFilter(contextHandler);
    }
}
