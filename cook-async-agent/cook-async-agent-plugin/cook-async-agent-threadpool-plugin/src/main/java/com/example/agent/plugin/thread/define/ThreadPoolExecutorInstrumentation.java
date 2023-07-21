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
 *
 */

package com.example.agent.plugin.thread.define;

import com.example.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import com.example.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import com.example.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import com.example.agent.core.plugin.match.ClassMatch;
import com.example.agent.core.plugin.match.HierarchyMatch;
import com.example.agent.core.plugin.match.MultiClassNameMatch;
import com.example.agent.core.plugin.match.logical.LogicalMatchOperation;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;


public class ThreadPoolExecutorInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS = "java.util.concurrent.ThreadPoolExecutor";

    private static final String INTERCEPT_EXECUTE_METHOD = "execute";

    private static final String INTERCEPT_SUBMIT_METHOD = "submit";

    private static final String INTERCEPT_EXECUTE_METHOD_HANDLE = "com.example.agent.plugin.thread.ThreadPoolExecuteMethodInterceptor";

    private static final String INTERCEPT_SUBMIT_METHOD_HANDLE = "com.example.agent.plugin.thread.ThreadPoolSubmitMethodInterceptor";

    @Override
    public boolean isBootstrapInstrumentation() {
        return true;
    }

    @Override
    protected ClassMatch enhanceClass() {
        return LogicalMatchOperation.or(HierarchyMatch.byHierarchyMatch(ENHANCE_CLASS), MultiClassNameMatch.byMultiClassMatch(ENHANCE_CLASS));
    }

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(INTERCEPT_EXECUTE_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPT_EXECUTE_METHOD_HANDLE;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return true;
                    }
                },
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(INTERCEPT_SUBMIT_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPT_SUBMIT_METHOD_HANDLE;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return true;
                    }
                }
        };
    }
}
