package com.example.stat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * @program: bjgoodwill-msa-scloud
 * @description:
 * @author: lk
 * @create: 2023-11-21
 **/

public class StatIndicator {
    
    private Counter apiRequestCounter;
    
    public StatIndicator(MeterRegistry registry) {
        /**
         * 创建请求数自定义指标
         * 计数器可以用于记录只会增加不会减少的指标类型，比如记录应用请求的总量(api_request_total)，
         * 一般而言，Counter类型的metrics指标在命名中，我们使用_total结束。
         */
        apiRequestCounter = registry.counter("api_request_total", "application", "log-service");
    }
    
    public void increment(){
        apiRequestCounter.increment();
    }
}
