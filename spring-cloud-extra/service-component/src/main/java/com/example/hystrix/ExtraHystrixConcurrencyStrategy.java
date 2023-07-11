package com.example.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.example.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @program: 解决hystrix多线程的隔离策略下，request无法传递的问题，以及Hystrix只允许有一个并发策略的问题
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Slf4j
public class ExtraHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {


    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        log.info("current thread wrapCallable: {}",Thread.currentThread().getName());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        Map<String, String> parameterMap = BaseParameterHolder.getParameterMap();
        return new WrappedCallable<>(callable, requestAttributes, copyOfContextMap, parameterMap);
    }

    static class WrappedCallable<T> implements Callable<T> {

        private final Callable<T> target;
        private final RequestAttributes requestAttributes;
        private final Map<String, String> context;
        
        private final Map<String, String> parameterMap;

        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes, Map<String, String> context, Map<String, String> parameterMap) {
            this.target = target;
            this.requestAttributes = requestAttributes;
            this.context = context;
            this.parameterMap = parameterMap;
        }

        @Override
        public T call() throws Exception {
            log.info("current thread call: {}",Thread.currentThread().getName());
            //log.info("RequestAttributeHystrixConcurrencyStrategy.WrappedCallable.call threadName:{} threadId:{}",Thread.currentThread().getName(),Thread.currentThread().getId());
            try {
                if (requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                }
                if (context != null) {
                    MDC.setContextMap(context);
                }
                if (parameterMap != null) {
                    BaseParameterHolder.setParameterMap(parameterMap);
                }
                return target.call();
            } finally {
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
                BaseParameterHolder.removeParameterMap();
            }
        }
    }
}
