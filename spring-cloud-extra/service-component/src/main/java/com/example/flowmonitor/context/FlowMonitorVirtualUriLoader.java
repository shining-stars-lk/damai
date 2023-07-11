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

package com.example.flowmonitor.context;

import com.example.flowmonitor.toolkit.Environments;
import com.example.flowmonitor.toolkit.Strings;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 微服务流量监控加载 URI
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public final class FlowMonitorVirtualUriLoader {
    
    private static final AtomicBoolean CONSUMER_LOAD_FLAG = new AtomicBoolean(Boolean.FALSE);
    private static final AtomicBoolean PROVIDER_LOAD_FLAG = new AtomicBoolean(Boolean.FALSE);
    
    /**
     * 加载来源客户端所有 URI
     */
    public static void loadConsumerUris() {
        if (!CONSUMER_LOAD_FLAG.get()) {
            synchronized (CONSUMER_LOAD_FLAG) {
                Set<String> allConsumerVirtualUris = loadActual();
                FlowMonitorRuntimeContext.CONSUMER_ALL_VIRTUAL_URIS.addAll(allConsumerVirtualUris);
                CONSUMER_LOAD_FLAG.set(Boolean.TRUE);
            }
        }
    }
    
    /**
     * 加载目标客户端所有 URI
     */
    public static void loadProviderUris() {
        if (!PROVIDER_LOAD_FLAG.get()) {
            synchronized (PROVIDER_LOAD_FLAG) {
                Set<String> allProviderUris = loadActual();
                FlowMonitorRuntimeContext.PROVIDER_ALL_VIRTUAL_URIS.addAll(allProviderUris);
                PROVIDER_LOAD_FLAG.set(Boolean.TRUE);
            }
        }
    }
    
    /**
     * 加载项目中所有 URI
     *
     * @return
     */
    private static Set<String> loadActual() {
        RequestMappingHandlerMapping mapping = ApplicationContextHolderProxy.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        Set<String> returnUriSet = new HashSet<>();
        String serverServletContextPath = Environments.getServerServletContextPath();
        for (RequestMappingInfo info : methodMap.keySet()) {
            Set<String> uriSet = info.getPatternsCondition().getPatterns();
            returnUriSet.addAll(uriSet.stream().map(each -> serverServletContextPath + getVirtualUri(each)).collect(Collectors.toSet()));
        }
        return returnUriSet;
    }
    
    /**
     * 通过真是 URI 映射虚拟 URI
     *
     * @param actualUri
     * @return
     */
    private static String getVirtualUri(String actualUri) {
        String[] paths = Strings.split(actualUri, "/");
        for (int i = 0; i < paths.length; i++) {
            String current = paths[i];
            if (Strings.startsWithIgnoreCase(current, "{") && Strings.endsWithIgnoreCase(current, "}")) {
                paths[i] = "*";
            }
        }
        return "/" + Strings.join(paths, "/");
    }
}
