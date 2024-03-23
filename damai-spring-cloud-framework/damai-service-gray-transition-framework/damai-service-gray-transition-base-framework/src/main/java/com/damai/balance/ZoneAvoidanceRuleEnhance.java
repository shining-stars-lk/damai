package com.damai.balance;


import com.google.common.base.Optional;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 负载均衡规则增强
 * @author: 阿宽不是程序员
 **/
public class ZoneAvoidanceRuleEnhance extends ZoneAvoidanceRule {
    

    @Override
    public Server choose(Object key) {
        ILoadBalancer loadBalancer = getLoadBalancer();
        List<Server> allServers = new ArrayList<>();
        java.util.Optional.ofNullable(loadBalancer.getAllServers()).ifPresent(allServers::addAll);
        Optional<Server> serverOptional = getPredicate().chooseRoundRobinAfterFiltering(allServers, key);
        return serverOptional.isPresent() ? serverOptional.get() : null;
    }
}