package com.extra.ribbon;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.util.Assert;

public class DiscoveryEnabledRule extends PredicateBasedRule {
    
    /**
     * 这里为静态的原因是ribbon在定时任务执行时是直接调用DiscoveryEnabledRule的构造方法，而不是从spring中获得
     * */
    private static String mark;
    
    private CompositePredicate predicate = null;
    
    public DiscoveryEnabledRule(ExtraRibbonProperties extraRibbonProperties){
        super();
        mark = extraRibbonProperties.getMark();
        MetadataAwarePredicate metadataAwarePredicate = new MetadataAwarePredicate(mark, this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
    
    public DiscoveryEnabledRule(){
        super();
        MetadataAwarePredicate metadataAwarePredicate = new MetadataAwarePredicate(mark, this);
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
