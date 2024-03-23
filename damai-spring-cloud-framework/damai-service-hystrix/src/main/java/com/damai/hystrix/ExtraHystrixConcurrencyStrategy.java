package com.damai.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.damai.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: hystrix线程池模式参数绑定
 * @author: 阿宽不是程序员
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
