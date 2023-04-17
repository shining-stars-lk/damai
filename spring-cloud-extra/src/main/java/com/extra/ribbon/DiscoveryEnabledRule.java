package com.extra.ribbon;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.util.Assert;

public class DiscoveryEnabledRule extends PredicateBasedRule {

    private final CompositePredicate predicate;
    
    public DiscoveryEnabledRule(){
        this(new MetadataAwarePredicate());
    }
    
    public DiscoveryEnabledRule(AbstractServerPredicate abstractServerPredicate) {
        Assert.notNull(abstractServerPredicate, "参数 'abstractServerPredicate' 不能为 null");
        this.predicate = createCompositePredicate(abstractServerPredicate, new AvailabilityPredicate(this, null));
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
