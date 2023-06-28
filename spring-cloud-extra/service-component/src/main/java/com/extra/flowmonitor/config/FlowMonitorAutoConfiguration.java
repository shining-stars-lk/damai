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

package com.extra.flowmonitor.config;


import com.extra.flowmonitor.common.FlowMonitorProperties;
import com.extra.flowmonitor.context.ApplicationContextHolderProxy;
import com.extra.flowmonitor.context.FlowMonitorEventHandler;
import com.extra.flowmonitor.context.FlowMonitorEventPush;
import com.extra.flowmonitor.filter.FlowMonitorFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "flow.monitor.enabled")
@EnableConfigurationProperties(FlowMonitorProperties.class)
public class FlowMonitorAutoConfiguration {
    
    @Bean
    public ApplicationContextHolderProxy applicationContextHolderProxy() {
        return new ApplicationContextHolderProxy();
    }
    
    @Bean
    public FlowMonitorEventPush flowMonitorEventPush(ApplicationContext applicationContext){
        return new FlowMonitorEventPush(applicationContext);
    }
    
    @Bean
    public FlowMonitorEventHandler flowMonitorEventHandler(){
        return new FlowMonitorEventHandler();
    }
    
    @Bean
    public FlowMonitorFilter flowMonitorFilter(){
        return new FlowMonitorFilter();
    }
}
