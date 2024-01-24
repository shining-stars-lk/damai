package com.example.multiplesubmitlimit.info.strategy.repeatrejected;


import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.TimeUnit;

public interface MultipleSubmitLimitHandler {

    Object execute(String resultKeyName, long timeOut, TimeUnit timeUnit, ProceedingJoinPoint joinPoint) throws Throwable;

    Object repeatRejected(String resultKeyName);

}
