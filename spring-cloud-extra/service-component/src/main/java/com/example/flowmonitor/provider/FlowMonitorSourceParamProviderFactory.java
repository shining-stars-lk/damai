/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flowmonitor.provider;

import com.example.flowmonitor.context.FlowMonitorEntity;
import com.example.flowmonitor.context.FlowMonitorRuntimeContext;
import com.example.flowmonitor.common.FlowMonitorConstant;
import com.example.flowmonitor.common.FlowMonitorFrameTypeEnum;
import com.wujiuye.flow.FlowHelper;
import com.wujiuye.flow.FlowType;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 微服务流量监控来源客户端参数提供工厂
 *
 * @program: 
 * @description:
 * @author: 星哥
 * @create: 2023-04-24
 **/
public final class FlowMonitorSourceParamProviderFactory {
    
    /**
     * 获取实例
     *
     * @param httpServletRequest Http 请求头
     * @return
     */
    public static FlowMonitorEntity getInstance(final HttpServletRequest httpServletRequest) {
        return getInstance(null, httpServletRequest);
    }
    
    /**
     * 获取实例
     *
     * @param customerTargetResource 自定义目标客户端资源信息, eg: XXL-Job、RocketMQ...
     * @param httpServletRequest     Http 请求头
     * @return
     */
    public static FlowMonitorEntity getInstance(final String customerTargetResource, final HttpServletRequest httpServletRequest) {
        return buildInstance(customerTargetResource, httpServletRequest, false, null);
    }
    
    /**
     * 获取实例
     *
     * @param customerTargetResource 自定义目标客户端资源信息, eg: XXL-Job、RocketMQ...
     * @param frameType              框架类型
     * @return
     */
    public static FlowMonitorEntity getInstance(final String customerTargetResource, final FlowMonitorFrameTypeEnum frameType) {
        return buildInstance(customerTargetResource, null, false, frameType);
    }
    
    /**
     * 创建实例
     *
     * @param customerTargetResource 自定义目标客户端资源信息, eg: XXL-Job、RocketMQ...
     * @return
     */
    public static FlowMonitorEntity createInstance(final String customerTargetResource, final FlowMonitorFrameTypeEnum frameType) {
        return createInstance(customerTargetResource, null, frameType);
    }
    
    /**
     * 创建实例
     *
     * @param httpServletRequest Http 请求头
     * @return
     */
    public static FlowMonitorEntity createInstance(final HttpServletRequest httpServletRequest) {
        return createInstance(null, httpServletRequest, null);
    }
    
    /**
     * 创建实例
     *
     * @param customerTargetResource 自定义目标客户端资源信息, eg: XXL-Job、RocketMQ...
     * @param httpServletRequest     Http 请求头
     * @return
     */
    public static FlowMonitorEntity createInstance(final String customerTargetResource, final HttpServletRequest httpServletRequest, final FlowMonitorFrameTypeEnum frameType) {
        return buildInstance(customerTargetResource, httpServletRequest, true, frameType);
    }
    
    /**
     * 构建实例
     *
     * @param customerTargetResource 自定义目标客户端资源信息, eg: XXL-Job、RocketMQ...
     * @param httpServletRequest     Http 请求头
     * @param createFlag             创建标识
     * @return
     */
    private static FlowMonitorEntity buildInstance(final String customerTargetResource,
                                                   final HttpServletRequest httpServletRequest,
                                                   final boolean createFlag,
                                                   final FlowMonitorFrameTypeEnum frameType) {
        
        String requestMethod = null;
        String sourceApplication = null;
        String sourceResource = null;
        String sourceIpPort = null;
        String targetResource = null;
        String flowMonitorType = null;
    
        if (httpServletRequest != null) {
            requestMethod = httpServletRequest.getMethod();
            sourceApplication = "Other";
            sourceResource = "Unknown";
            sourceIpPort = "Unknown";
            targetResource = httpServletRequest.getRequestURI();
            flowMonitorType = "API";
            targetResource = FlowMonitorRuntimeContext.getProvideVirtualUri(targetResource);
            String apiVersion = httpServletRequest.getHeader(FlowMonitorConstant.API_VERSION);
            if (StringUtils.isNotBlank(apiVersion)) {
                targetResource = targetResource + "_" + apiVersion;
            }
        }
        FlowMonitorEntity monitorEntity = FlowMonitorEntity.builder()
                .type(flowMonitorType)
                .requestMethod(requestMethod)
                .sourceApplication(sourceApplication)
                .sourceResource(sourceResource)
                .sourceIpPort(sourceIpPort)
                .targetResource(targetResource)
                .build();
        if (createFlag) {
            monitorEntity.setFlowHelper(new FlowHelper(FlowType.Minute));
        }
        return monitorEntity;
    }
}
