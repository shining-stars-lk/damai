package com.extra.ribbon;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class DiscoveryEnabledRule extends PredicateBasedRule implements InitializingBean {

    private CompositePredicate predicate = null;
    
    private ExtraRibbonProperties extraRibbonProperties;
    
    public DiscoveryEnabledRule(ExtraRibbonProperties extraRibbonProperties){
        this.extraRibbonProperties = extraRibbonProperties;
    }
    
    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }
    
    private CompositePredicate createCompositePredicate(AbstractServerPredicate abstractServerPredicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(abstractServerPredicate, availabilityPredicate)
                .build();
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        MetadataAwarePredicate metadataAwarePredicate = new MetadataAwarePredicate(extraRibbonProperties.getMark(), this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
}
