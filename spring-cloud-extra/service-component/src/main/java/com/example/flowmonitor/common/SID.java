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

import com.example.flowmonitor.context.ApplicationContextHolderProxy;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * @program: 
 * @description:
 * @author: 星哥
 * @create: 2023-04-24
 **/
public class SID {
    
    private static String host;
    
    private static int port;
    
    static {
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        host = localHost.getHostAddress();
        port = Optional.ofNullable(ApplicationContextHolderProxy.getBean(ServerProperties.class).getPort()).orElse(8080);
    }
    
    public static String getIpAddressAndPort() {
        return new StringBuilder().append(host).append(":").append(port).toString();
    }
}
