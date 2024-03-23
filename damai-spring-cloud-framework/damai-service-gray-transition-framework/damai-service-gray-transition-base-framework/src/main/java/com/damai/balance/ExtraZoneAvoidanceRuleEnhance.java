package com.damai.balance;


import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import lombok.Getter;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 灰度版本选择负载均衡选择器适配
 * @author: 阿宽不是程序员
 **/
public class ExtraZoneAvoidanceRuleEnhance extends ZoneAvoidanceRuleEnhance {
    private CompositePredicate compositePredicate;
    @Getter
    private ExtraZoneAvoidancePredicate extraZoneAvoidancePredicate;
    
    /**
     * 使用无参构造方法的原因是Ribbon在定时任务中，会创建此适配器，而创建的方法是使用反射来构建
     * */
    public ExtraZoneAvoidanceRuleEnhance() {
        super();
        init(null);
    }

    private CompositePredicate createCompositePredicate(ExtraZoneAvoidancePredicate cookPatchEnabledPredicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(cookPatchEnabledPredicate, availabilityPredicate)
                .build();
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        init(clientConfig);
    }

    public void init(IClientConfig clientConfig){
        extraZoneAvoidancePredicate = new ExtraZoneAvoidancePredicate(this, clientConfig);
        AvailabilityPredicate availabilityPredicate = new AvailabilityPredicate(this, clientConfig);
        compositePredicate = createCompositePredicate(extraZoneAvoidancePredicate, availabilityPredicate);
    }
    
    /**
     * 当请求执行到Ribbon时，会执行到 过滤器适配器的获得过滤器方法 {@link PredicateBasedRule#choose(Object)}
     * */
    @Override
    public AbstractServerPredicate getPredicate() {
        return compositePredicate;
    }
    
}