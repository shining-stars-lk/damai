package com.example.handler;


import com.example.constant.KoConstant;
import com.example.util.Common;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * zhangchang
 */
@Aspect
@Component
public class ComputeTimeHandler {
    private static Logger log = Logger.getLogger(ComputeTimeHandler.class.toString());

    @Pointcut(KoConstant.comMethodRange)
    public void preProcess() {
    }

    @Around("preProcess()")
    public Object doAroundCompute(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime();
        Common.showLog(pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName(), ((end - begin) / 1000000));
        return obj;
    }
}
