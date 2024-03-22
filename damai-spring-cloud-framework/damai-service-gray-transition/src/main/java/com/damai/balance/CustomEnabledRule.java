package com.damai.balance;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.util.Assert;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 灰度版本选择负载均衡选择器适配
 * @author: 阿宽不是程序员
 **/
public class CustomEnabledRule extends PredicateBasedRule {
    
    /**
     * 这里为静态的原因是ribbon在定时任务执行时是直接调用DiscoveryEnabledRule的无参构造方法，而不是从spring中获得
     * */
    private static String gray;
    
    private final CompositePredicate predicate;
    
    public CustomEnabledRule(String serverGray){
        super();
        gray = serverGray;
        CustomAwarePredicate metadataAwarePredicate = new CustomAwarePredicate(gray, this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
    
    /**
     * 保留此构造方法的原因是Ribbon在定时任务中，会创建此适配器，而创建的方法是使用反射来构建
     * */
    public CustomEnabledRule(){
        super();
        CustomAwarePredicate metadataAwarePredicate = new CustomAwarePredicate(gray, this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
    /**
     * 当请求执行到Ribbon时，会执行到 过滤器适配器的获得过滤器方法 {@link PredicateBasedRule#choose(Object)}
     * */
    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }
    
    private CompositePredicate createCompositePredicate(AbstractServerPredicate abstractServerPredicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(abstractServerPredicate, availabilityPredicate)
                .build();
    }
}
