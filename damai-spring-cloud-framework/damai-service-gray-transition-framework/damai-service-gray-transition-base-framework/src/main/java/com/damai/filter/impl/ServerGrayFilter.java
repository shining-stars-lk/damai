package com.damai.filter.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.damai.context.ContextHandler;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.filter.AbstractServerFilter;
import com.damai.threadlocal.BaseParameterHolder;
import com.damai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.damai.constant.Constant.GRAY_FLAG_FALSE;
import static com.damai.constant.Constant.GRAY_FLAG_TRUE;
import static com.damai.constant.Constant.GRAY_PARAMETER;
import static com.damai.constant.Constant.SERVER_GRAY;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 灰度过滤器
 * @author: 阿星不是程序员
 **/

@Slf4j
public class ServerGrayFilter extends AbstractServerFilter {
    
    /**
     * 此服务的灰度标识
     * */
    @Value(SERVER_GRAY)
    private String serverGray;
    
    private final ContextHandler contextHandler;
    
    private final Map<String,String> map = new HashMap<>();
    
    public ServerGrayFilter(ContextHandler contextHandler){
        this.contextHandler = contextHandler;
        this.map.put(GRAY_FLAG_FALSE, GRAY_FLAG_FALSE);
        this.map.put(GRAY_FLAG_TRUE, GRAY_FLAG_TRUE);
    }
    

    @Override
    public boolean doFilter(List<? extends ServiceInstance> servers, ServiceInstance server) {
        boolean result;
        try {
            String grayFromRequest = Optional.ofNullable(contextHandler.getValueFromHeader(GRAY_PARAMETER))
                    .filter(StringUtil::isNotEmpty)
                    .orElseGet(() -> BaseParameterHolder.getParameter(GRAY_PARAMETER));
            grayFromRequest = Optional.ofNullable(grayFromRequest).filter(StringUtil::isNotEmpty).orElse(serverGray);
            NacosServiceInstance nacosServiceInstance = (NacosServiceInstance)server;
            String grayFromMetaData = Optional.ofNullable(nacosServiceInstance.getMetadata())
                    .filter(CollectionUtil::isNotEmpty)
                    .map(metadata -> metadata.get(GRAY_PARAMETER))
                    .filter(StringUtil::isNotEmpty)
                    .orElse(GRAY_FLAG_FALSE);
            grayFromMetaData = Optional.ofNullable(map.get(grayFromMetaData.toLowerCase())).orElse(GRAY_FLAG_FALSE);
            grayFromRequest = Optional.ofNullable(map.get(grayFromRequest.toLowerCase())).orElse(GRAY_FLAG_FALSE);
            result = grayFromMetaData.equalsIgnoreCase(grayFromRequest);

            if (!result && grayFromRequest.equalsIgnoreCase(GRAY_FLAG_TRUE)) {
                if (CollectionUtil.isEmpty(servers)) {
                    throw new DaMaiFrameException(BaseCode.SERVER_LIST_NOT_EXIST);
                }
                Map<String,String> map = new HashMap<>(servers.size());
                for (ServiceInstance serviceInstance : servers) {
                    NacosServiceInstance instance = (NacosServiceInstance)serviceInstance;
                    String balanceGray = instance.getMetadata().get(GRAY_PARAMETER);
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

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}