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

package com.example.flowmonitor.writer;

import com.example.flowmonitor.context.FlowMonitorRuntimeContext;
import com.example.flowmonitor.toolkit.Environments;
import com.example.flowmonitor.common.SID;
import com.wujiuye.flow.FlowType;
import com.wujiuye.flow.Flower;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 微服务流量监控写入器
 *
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public final class FlowMonitorWrite {
    
    private final static ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);
    private final static AtomicBoolean INIT_FLAG = new AtomicBoolean(Boolean.FALSE);
    
    /**
     * 初始化定时写入流量监控数据
     */
    public static void initScheduleWriteData() {
        if (!INIT_FLAG.get()) {
            synchronized (FlowMonitorWrite.class) {
                SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(() -> FlowMonitorRuntimeContext.STORAGE.forEach((interfaceKey, val) -> {
                    // System.out.println("------------------------------------------");
                    // System.out.println(String.format("------------ 目标接口: %s", interfaceKey));
                    val.forEach((sourceApplication, hosts) -> hosts.forEach((host, param) -> {
                        Flower flower = param.getFlowHelper().getFlow(FlowType.Minute);
                        FlowMonitorRunState runState = FlowMonitorRunState.builder()
                                .targetApplication(Environments.getApplicationName())
                                .targetIpPort(SID.getIpAddressAndPort())
                                .targetResource(interfaceKey)
                                .sourceApplication(sourceApplication)
                                .sourceIpPort(host)
                                .sourceResource(param.getSourceResource())
                                .total(flower.total())
                                .totalSuccess(flower.totalSuccess())
                                .totalException(flower.totalException())
                                .avgRt(flower.avgRt())
                                .maxRt(flower.maxRt())
                                .minRt(flower.minRt())
                                .successAvg(flower.successAvg())
                                .exceptionAvg(flower.exceptionAvg())
                                .type(param.getType())
                                .build();
                        MicrometerStorageExecutor.execute(runState);
                        // System.out.println(String.format("------------ 来源应用: %s", sourceApplication));
                        // System.out.println(String.format("------------ 来源接口: %s", param.getSourceResource()));
                        // System.out.println(String.format("------------ 来源 Host: %s", host));
                        // System.out.println("------------------------------------------");
                        // System.out.println("总请求数: " + flower.total());
                        // System.out.println("成功请求数: " + flower.totalSuccess());
                        // System.out.println("异常请求数: " + flower.totalException());
                        // System.out.println("平均请求耗时: " + flower.avgRt());
                        // System.out.println("最大请求耗时: " + flower.maxRt());
                        // System.out.println("最小请求耗时: " + flower.minRt());
                        // System.out.println("平均请求成功数: " + flower.successAvg());
                        // System.out.println("平均请求异常数: " + flower.exceptionAvg());
                    }));
                }), 0, 6, TimeUnit.SECONDS);
                INIT_FLAG.set(Boolean.TRUE);
            }
        }
    }
}
