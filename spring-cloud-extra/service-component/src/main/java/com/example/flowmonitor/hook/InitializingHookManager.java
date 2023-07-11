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

package com.example.flowmonitor.hook;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 初始化钩子函数管理
 *
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public enum InitializingHookManager {
    
    INSTANCE;
    
    private Map<String, InitializingHook> initializingHooks = Collections.emptyMap();
    
    public void boot() {
        initializingHooks = loadInitializingHooks();
        initializingHooks.values().forEach(each -> {
            try {
                each.afterAgentPremain();
            } catch (Exception ignored) {
            }
        });
    }
    
    public Map<String, InitializingHook> loadInitializingHooks() {
        Map<String, InitializingHook> allInitializingHooks = new LinkedHashMap<>();
        allInitializingHooks.put(InitializingProfilesActiveHook.class.getName(), new InitializingProfilesActiveHook());
        return allInitializingHooks;
    }
}
