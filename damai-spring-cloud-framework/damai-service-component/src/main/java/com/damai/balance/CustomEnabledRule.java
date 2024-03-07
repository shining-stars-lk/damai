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
    
    private CompositePredicate predicate = null;
    
    public CustomEnabledRule(ExtraRibbonProperties extraRibbonProperties){
        super();
        gray = extraRibbonProperties.getGray();
        CustomAwarePredicate metadataAwarePredicate = new CustomAwarePredicate(gray, this);
        Assert.notNull(metadataAwarePredicate, "参数 'abstractServerPredicate' 不能为 null");
        predicate = createCompositePredicate(metadataAwarePredicate, new AvailabilityPredicate(this, null));
    }
    
    public CustomEnabledRule(){
        super();
        CustomAwarePredicate metadataAwarePredicate = new CustomAwarePredicate(gray, this);
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
