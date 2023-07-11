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

package com.example.flowmonitor.toolkit;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 系统时钟<br>
 * 高并发场景下 System.currentTimeMillis() 的性能问题的优化
 *
 * @program: 
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public final class SystemClock {
    
    /**
     * 时钟更新间隔，单位毫秒
     */
    private final long period;
    
    /**
     * 现在时刻的毫秒数
     */
    private volatile long now;
    
    /**
     * 构造
     *
     * @param period 时钟更新间隔，单位毫秒
     */
    public SystemClock(long period) {
        this.period = period;
        this.now = System.currentTimeMillis();
        scheduleClockUpdating();
    }
    
    /**
     * 开启计时器线程
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "System Clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now = System.currentTimeMillis(), period, period, TimeUnit.MILLISECONDS);
    }
    
    /**
     * @return 当前时间毫秒数
     */
    private long currentTimeMillis() {
        return now;
    }
    
    /**
     * 单例
     *
     * @author Looly
     */
    private static class InstanceHolder {
        
        public static final SystemClock INSTANCE = new SystemClock(1);
    }
    
    /**
     * @return 当前时间
     */
    public static long now() {
        return InstanceHolder.INSTANCE.currentTimeMillis();
    }
    
    /**
     * @return 当前时间字符串表现形式
     */
    public static String nowDate() {
        return new Timestamp(InstanceHolder.INSTANCE.currentTimeMillis()).toString();
    }
}
