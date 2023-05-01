package com.extra.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @program: 解决hystrix多线程的隔离策略下，request无法传递的问题，以及Hystrix只允许有一个并发策略的问题
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Slf4j
public class ExtraHystrixConcurrencyStrategyV2 extends HystrixConcurrencyStrategy {


    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        return new WrappedCallable<>(callable, requestAttributes, copyOfContextMap);
    }

    static class WrappedCallable<T> implements Callable<T> {

        private final Callable<T> target;
        private final RequestAttributes requestAttributes;
        private final Map<String, String> context;

        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes, Map<String, String> context) {
            this.target = target;
            this.requestAttributes = requestAttributes;
            this.context = context;
        }

        @Override
        public T call() throws Exception {
            //log.info("RequestAttributeHystrixConcurrencyStrategy.WrappedCallable.call threadName:{} threadId:{}",Thread.currentThread().getName(),Thread.currentThread().getId());
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                //MDC.setContextMap(context);
                return target.call();
            } finally {
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
