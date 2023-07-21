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

package com.example.agent.plugin.thread;


import com.example.agent.plugin.thread.wrapper.CallableWrapper;
import com.example.agent.plugin.thread.wrapper.RunnableWrapper;

import java.util.Map;
import java.util.concurrent.Callable;

public class ThreadPoolSubmitMethodInterceptor extends AbstractThreadingPoolInterceptor {
    
    @Override
    public Object wrap(final Object param) {
        if (param instanceof RunnableWrapper || param instanceof CallableWrapper) {
            return null;
        }
        if (param instanceof Callable) {
            Callable callable = (Callable) param;
            Map<String, String> contextForTask = getContextForTask();
            Map<String, String> contextForHold = getContextForHold();
            return new CallableWrapper(callable,contextForTask,contextForHold);
        }
        if (param instanceof Runnable) {
            Runnable runnable = (Runnable) param;
            Map<String, String> contextForTask = getContextForTask();
            Map<String, String> contextForHold = getContextForHold();
            return new RunnableWrapper(runnable,contextForTask,contextForHold);
        }
        return null;
    }
}
