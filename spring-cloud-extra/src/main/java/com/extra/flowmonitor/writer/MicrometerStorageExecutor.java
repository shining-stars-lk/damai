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

package com.extra.flowmonitor.writer;

import com.extra.flowmonitor.toolkit.Lists;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对接指标存储模式
 *
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public final class MicrometerStorageExecutor {
    
    private static final String METRIC_NAME_PREFIX = "flow.monitor";
    
    private static final String METRIC_APPLICATION_NAME_TAG = METRIC_NAME_PREFIX + ".application.name";
    
    private static final String METRIC_HOST_TAG = METRIC_NAME_PREFIX + ".host";
    
    private static final String METRIC_RESOURCE_TAG = METRIC_NAME_PREFIX + ".resource";
    
    private static final String METRIC_TYPE_TAG = METRIC_NAME_PREFIX + ".type";
    
    private static final String METRIC_CONSUMER_APPLICATION_NAME_TAG = METRIC_NAME_PREFIX + ".source.application.name";
    
    private static final Map<String, FlowMonitorRunState> RUN_STATE_CACHE = new ConcurrentHashMap<>();
    
    public static void execute(FlowMonitorRunState runState) {
        String key = runState.getTargetApplication() + runState.getTargetResource() + runState.getSourceApplication() + runState.getSourceResource();
        FlowMonitorRunState originalRunState = RUN_STATE_CACHE.get(key);
        if (originalRunState != null) {
            originalRunState.setTargetIpPort(runState.getTargetIpPort());
            originalRunState.setTargetResource(runState.getTargetResource());
            originalRunState.setTargetApplication(runState.getTargetApplication());
            originalRunState.setSourceIpPort(runState.getSourceIpPort());
            originalRunState.setSourceResource(runState.getSourceResource());
            originalRunState.setSourceApplication(runState.getSourceApplication());
            originalRunState.setTotal(runState.getTotal());
            originalRunState.setExceptionAvg(runState.getExceptionAvg());
            originalRunState.setAvgRt(runState.getAvgRt());
            originalRunState.setMaxRt(runState.getMaxRt());
            originalRunState.setMinRt(runState.getMinRt());
            originalRunState.setSuccessAvg(runState.getSuccessAvg());
            originalRunState.setTotalSuccess(runState.getTotalSuccess());
            originalRunState.setTotalException(runState.getTotalException());
            originalRunState.setType(runState.getType());
        } else {
            RUN_STATE_CACHE.put(key, runState);
        }
        Iterable<Tag> tags = Lists.newArrayList(
                Tag.of(METRIC_TYPE_TAG, runState.getType()),
                Tag.of(METRIC_RESOURCE_TAG, runState.getTargetResource()),
                Tag.of(METRIC_APPLICATION_NAME_TAG, runState.getTargetApplication()),
                Tag.of(METRIC_HOST_TAG, runState.getTargetIpPort()),
                Tag.of(METRIC_CONSUMER_APPLICATION_NAME_TAG, runState.getSourceApplication()));
        Metrics.gauge(metricName("total"), tags, runState, FlowMonitorRunState::getTotal);
        Metrics.gauge(metricName("total.success"), tags, runState, FlowMonitorRunState::getTotalSuccess);
        Metrics.gauge(metricName("total.exception"), tags, runState, FlowMonitorRunState::getTotalException);
        Metrics.gauge(metricName("min.rt"), tags, runState, FlowMonitorRunState::getMinRt);
        Metrics.gauge(metricName("max.rt"), tags, runState, FlowMonitorRunState::getMaxRt);
        Metrics.gauge(metricName("avg.rt"), tags, runState, FlowMonitorRunState::getAvgRt);
        Metrics.gauge(metricName("success.avg"), tags, runState, FlowMonitorRunState::getSuccessAvg);
        Metrics.gauge(metricName("exception.avg"), tags, runState, FlowMonitorRunState::getExceptionAvg);
    }
    
    private static String metricName(String name) {
        return String.join(".", METRIC_NAME_PREFIX, name);
    }
}
