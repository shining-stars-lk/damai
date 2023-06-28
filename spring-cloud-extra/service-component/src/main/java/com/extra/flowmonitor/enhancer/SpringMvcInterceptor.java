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

package com.extra.flowmonitor.enhancer;


import com.extra.flowmonitor.context.FlowMonitorEntity;
import com.extra.flowmonitor.context.FlowMonitorRuntimeContext;
import com.extra.flowmonitor.provider.FlowMonitorSourceParamProviderFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring MVC 增强
 *
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public final class SpringMvcInterceptor{
    
    public static void loadResource(HttpServletRequest httpServletRequest) {
        FlowMonitorEntity sourceParam = FlowMonitorSourceParamProviderFactory.createInstance(httpServletRequest);
        Map<String, Map<String, FlowMonitorEntity>> sourceApplications;
        if ((sourceApplications = FlowMonitorRuntimeContext.getApplications(sourceParam.getTargetResource())) == null) {
            sourceApplications = new ConcurrentHashMap<>();
            Map<String, FlowMonitorEntity> hosts = new ConcurrentHashMap<>();
            hosts.put(sourceParam.getSourceIpPort(), sourceParam);
            sourceApplications.put(sourceParam.getSourceApplication(), hosts);
            FlowMonitorRuntimeContext.putApplications(sourceParam.getTargetResource(), sourceApplications);
        } else if (FlowMonitorRuntimeContext.getHosts(sourceParam.getTargetResource(), sourceParam.getSourceApplication()) == null) {
            Map<String, FlowMonitorEntity> hosts = new ConcurrentHashMap<>();
            hosts.put(sourceParam.getSourceIpPort(), sourceParam);
            sourceApplications.put(sourceParam.getSourceApplication(), hosts);
            FlowMonitorRuntimeContext.putHosts(sourceParam.getTargetResource(), sourceParam.getSourceApplication(), hosts);
        } else if (FlowMonitorRuntimeContext.getHost(sourceParam.getTargetResource(), sourceParam.getSourceApplication(), sourceParam.getSourceIpPort()) == null) {
            FlowMonitorRuntimeContext.putHost(sourceParam.getTargetResource(), sourceParam.getSourceApplication(), sourceParam.getSourceIpPort(), sourceParam);
        }
    }
}
