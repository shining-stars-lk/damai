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

package com.extra.flowmonitor.toolkit;

import com.extra.flowmonitor.context.ApplicationContextHolderProxy;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;

/**
 * 应用环境工具类
 *
 * @program: 
 * @description:
 * @author: lk
 * @create: 2023-04-24
 **/
public class Environments {
    
    public static String getApplicationName() {
        return ApplicationContextHolderProxy.getBean(ConfigurableEnvironment.class).getProperty("spring.application.name", "");
    }
    
    public static String getSpringProfilesActive() {
        return ApplicationContextHolderProxy.getBean(ConfigurableEnvironment.class).getProperty("spring.profiles.active", "");
    }
    
    public static String getServerServletContextPath() {
        String serverServletContextPath = ApplicationContextHolderProxy.getBean(ConfigurableEnvironment.class).getProperty("server.servlet.context-path", "");
        if (Strings.isNotEmpty(serverServletContextPath)) {
            String substring = serverServletContextPath.substring(serverServletContextPath.length() - 1, serverServletContextPath.length());
            if (Objects.equals("/", substring)) {
                serverServletContextPath = serverServletContextPath.substring(0, serverServletContextPath.length() - 1);
            }
        }
        return serverServletContextPath;
    }
}
