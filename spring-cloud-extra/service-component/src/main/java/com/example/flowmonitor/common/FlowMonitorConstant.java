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

package com.example.flowmonitor.common;

/**
 * @program: 
 * @description:
 * @author: 星哥
 * @create: 2023-04-24
 **/
public class FlowMonitorConstant {
    
    /**
     * Spring 客户端服务名
     */
    public static final String SPRING_APPLICATION_NAME = "spring.application.name";
    
    /**
     * 客户端传递 Spring 服务名 Key
     */
    public static final String SOURCE_APPLICATION_NAME = "flow.monitor.source.application.name";
    
    /**
     * 客户端传递 HTTP Method Key
     */
    public static final String SOURCE_HTTP_REQUEST_METHOD = "flow.monitor.source.request.method";
    
    /**
     * 客户端传递 HTTP URI Key
     */
    public static final String SOURCE_HTTP_REQUEST_URI = "flow.monitor.source.request.uri";
    
    /**
     * 客户端传递 Host Key
     */
    public static final String SOURCE_HTTP_HOST = "flow.monitor.source.host";
    
    /**
     * 网关调用标识
     */
    public static final String SOURCE_GATEWAY_FLAG = "flow.monitor.source.gateway.flag";
    
    /**
     * 服务端被调用 HTTP URI Key
     */
    public static final String TARGET_HTTP_REQUEST_URI = "flow.monitor.target.request.uri";
    
    /**
     * api-version
     * */
    public static final String API_VERSION = "api-version";
}
