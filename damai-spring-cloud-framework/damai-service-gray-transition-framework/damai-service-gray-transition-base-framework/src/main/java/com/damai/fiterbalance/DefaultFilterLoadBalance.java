package com.damai.fiterbalance;


import com.damai.balance.FilterLoadBalance;
import com.damai.filter.AbstractServerFilter;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 负载均衡服务过滤接口的实现
 * @author: 阿星不是程序员
 **/
@AllArgsConstructor
public class DefaultFilterLoadBalance implements FilterLoadBalance {

    protected final List<AbstractServerFilter> strategyFilterList;

    @Override
    public void selectServer(List<ServiceInstance> servers) {
        for (AbstractServerFilter strategyEnabledFilter : strategyFilterList) {
            strategyEnabledFilter.filter(servers);
        }
    }
}