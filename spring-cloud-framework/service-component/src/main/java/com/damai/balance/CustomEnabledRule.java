package com.damai.balance;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.util.Assert;

public class CustomEnabledRule extends PredicateBasedRule {
    
    /**
     * 这里为静态的原因是ribbon在定时任务执行时是直接调用DiscoveryEnabledRule的无参构造方法，而不是从spring中获得
     * */
    private static String mark;
    
    private CompositePredicate predicate = null;
    
    public CustomEnabledRule(ExtraRibbonProperties extraRibbonProperties){
        super();
        mark = extraRibbonProperties.getMark();
        CustomAwarePredicate metadataAwarePredicate = new CustomAwarePredicate(mark, this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
    
    public CustomEnabledRule(){
        super();
        CustomAwarePredicate metadataAwarePredicate = new CustomAwarePredicate(mark, this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
    
    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }
    
    private CompositePredicate createCompositePredicate(AbstractServerPredicate abstractServerPredicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(abstractServerPredicate, availabilityPredicate)
                .build();
    }
}
