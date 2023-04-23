//package com.extra.hystrix;
//
//import com.netflix.hystrix.HystrixThreadPoolKey;
//import com.netflix.hystrix.HystrixThreadPoolProperties;
//import com.netflix.hystrix.strategy.HystrixPlugins;
//import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
//import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
//import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
//import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
//import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
//import com.netflix.hystrix.strategy.properties.HystrixProperty;
//import lombok.extern.log4j.Log4j2;
//import org.slf4j.MDC;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @program: 解决hystrix多线程的隔离策略下，request无法传递的问题，以及Hystrix只允许有一个并发策略的问题
// * @description:
// * @author: lk
// * @create: 2023-04-17
// **/
//@Slf4j
//public class ExtraHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
//
//    private HystrixConcurrencyStrategy delegate;
//
//    public ExtraHystrixConcurrencyStrategy() {
//        try {
//            this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
//            if (this.delegate instanceof ExtraHystrixConcurrencyStrategy) {
//                // Welcome to singleton hell...
//                return;
//            }
//            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins
//                    .getInstance().getCommandExecutionHook();
//            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance()
//                    .getEventNotifier();
//            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance()
//                    .getMetricsPublisher();
//            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance()
//                    .getPropertiesStrategy();
//            this.logCurrentStateOfHystrixPlugins(eventNotifier, metricsPublisher,
//                    propertiesStrategy);
//            HystrixPlugins.reset();
//            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
//            HystrixPlugins.getInstance()
//                    .registerCommandExecutionHook(commandExecutionHook);
//            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
//            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
//            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
//        }
//        catch (Exception e) {
//            log.error("Failed to register Sleuth Hystrix Concurrency Strategy", e);
//        }
//    }
//
//    private void logCurrentStateOfHystrixPlugins(HystrixEventNotifier eventNotifier,
//                                                 HystrixMetricsPublisher metricsPublisher,
//                                                 HystrixPropertiesStrategy propertiesStrategy) {
//        if (log.isDebugEnabled()) {
//            log.debug("Current Hystrix plugins configuration is ["
//                    + "concurrencyStrategy [" + this.delegate + "]," + "eventNotifier ["
//                    + eventNotifier + "]," + "metricPublisher [" + metricsPublisher + "],"
//                    + "propertiesStrategy [" + propertiesStrategy + "]," + "]");
//            log.debug("Registering Sleuth Hystrix Concurrency Strategy.");
//        }
//    }
//
//    @Override
//    public <T> Callable<T> wrapCallable(Callable<T> callable) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
//        return new WrappedCallable<>(callable, requestAttributes, copyOfContextMap);
//    }
//
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
//                                            HystrixProperty<Integer> corePoolSize,
//                                            HystrixProperty<Integer> maximumPoolSize,
//                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
//                                            BlockingQueue<Runnable> workQueue) {
//        return this.delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
//                keepAliveTime, unit, workQueue);
//    }
//
//    @Override
//    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
//                                            HystrixThreadPoolProperties threadPoolProperties) {
//        return this.delegate.getThreadPool(threadPoolKey, threadPoolProperties);
//    }
//
//    @Override
//    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
//        return this.delegate.getBlockingQueue(maxQueueSize);
//    }
//
//    @Override
//    public <T> HystrixRequestVariable<T> getRequestVariable(
//            HystrixRequestVariableLifecycle<T> rv) {
//        return this.delegate.getRequestVariable(rv);
//    }
//
//    static class WrappedCallable<T> implements Callable<T> {
//
//        private final Callable<T> target;
//        private final RequestAttributes requestAttributes;
//        private final Map<String, String> context;
//
//        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes, Map<String, String> context) {
//            this.target = target;
//            this.requestAttributes = requestAttributes;
//            this.context = context;
//        }
//
//        @Override
//        public T call() throws Exception {
//            //log.info("RequestAttributeHystrixConcurrencyStrategy.WrappedCallable.call threadName:{} threadId:{}",Thread.currentThread().getName(),Thread.currentThread().getId());
//            try {
//                RequestContextHolder.setRequestAttributes(requestAttributes);
//                //MDC.setContextMap(context);
//                return target.call();
//            } finally {
//                MDC.clear();
//                RequestContextHolder.resetRequestAttributes();
//            }
//        }
//    }
//}
