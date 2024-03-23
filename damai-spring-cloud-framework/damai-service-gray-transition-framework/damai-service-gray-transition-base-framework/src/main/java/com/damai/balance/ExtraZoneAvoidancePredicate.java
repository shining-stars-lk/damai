package com.damai.balance;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.damai.context.ContextHandler;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.threadlocal.BaseParameterHolder;
import com.damai.util.StringUtil;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidancePredicate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.damai.constant.Constant.GRAY_FLAG_FALSE;
import static com.damai.constant.Constant.GRAY_FLAG_TRUE;
import static com.damai.constant.Constant.GRAY_PARAMETER;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 灰度版本选择负载均衡选择器
 * @author: 阿宽不是程序员
 **/
@Slf4j
@Setter
public class ExtraZoneAvoidancePredicate extends ZoneAvoidancePredicate {
    
    private String serverGray;
    
    private ContextHandler contextHandler;
    
    private final Map<String,String> map = new HashMap<>();

    public ExtraZoneAvoidancePredicate(IRule rule, IClientConfig clientConfig) {
        super(rule, clientConfig);
        this.map.put(GRAY_FLAG_FALSE, GRAY_FLAG_FALSE);
        this.map.put(GRAY_FLAG_TRUE, GRAY_FLAG_TRUE);
    }
    
    @Override
    public boolean apply(PredicateKey input) {
            boolean result;
            try {
                String grayFromRequest = Optional.ofNullable(contextHandler.getValueFromHeader(GRAY_PARAMETER))
                        .filter(StringUtil::isNotEmpty)
                        .orElseGet(() -> BaseParameterHolder.getParameter(GRAY_PARAMETER));
                grayFromRequest = Optional.ofNullable(grayFromRequest).filter(StringUtil::isNotEmpty).orElse(serverGray);
                NacosServer nacosServer = (NacosServer) input.getServer();
                String grayFromMetaData = Optional.ofNullable(nacosServer.getInstance().getMetadata())
                        .filter(CollectionUtil::isNotEmpty)
                        .map(metadata -> metadata.get(GRAY_PARAMETER))
                        .filter(StringUtil::isNotEmpty)
                        .orElse(GRAY_FLAG_FALSE);
                grayFromMetaData = Optional.ofNullable(map.get(grayFromMetaData.toLowerCase())).orElse(GRAY_FLAG_FALSE);
                grayFromRequest = Optional.ofNullable(map.get(grayFromRequest.toLowerCase())).orElse(GRAY_FLAG_FALSE);
                result = grayFromMetaData.equalsIgnoreCase(grayFromRequest);

                if (!result && grayFromRequest.equalsIgnoreCase(GRAY_FLAG_TRUE)) {
                    List<Server> servers = Optional.ofNullable(rule.getLoadBalancer())
                            .map(ILoadBalancer::getReachableServers)
                            .filter(CollectionUtil::isNotEmpty)
                            .orElseThrow(() -> new DaMaiFrameException(BaseCode.SERVER_LIST_NOT_EXIST));
                    Map<String,String> map = new HashMap<>(servers.size());
                    for (Server server : servers) {
                        NacosServer nacosServerInstance = (NacosServer) server;
                        String balanceGray = nacosServerInstance.getInstance().getMetadata().get(GRAY_PARAMETER);
                        if (StringUtil.isEmpty(balanceGray) || Objects.isNull(map.get(balanceGray.toLowerCase()))) {
                            balanceGray = GRAY_FLAG_FALSE;
                        }
                        map.put(balanceGray,balanceGray);
                    }
                    if(Objects.isNull(map.get(GRAY_FLAG_TRUE))) {
                        result = true;
                    }
                }
            }catch (Exception e) {
                result = false;
                log.error("CustomAwarePredicate#apply error",e);
            } 
            return result;
    }
}