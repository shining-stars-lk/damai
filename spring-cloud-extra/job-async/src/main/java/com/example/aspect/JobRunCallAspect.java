package com.example.aspect;

import com.example.BusinessThreadPool;
import com.example.annotation.JobCall;
import com.example.callback.JobRunCallBack;
import com.example.enums.JobRunStatus;
import com.example.enums.JobRunType;
import com.example.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.example.constant.Constant.JOB_INFO_ID;
import static com.example.constant.Constant.JOB_RUN_RECORD_ID;

/**
 * @program: toolkit
 * @description: 任务异步执行切面配置类,被注解修饰的方法能够异步执行并保证事务
 * @author: k
 * @create: 2023-06-28
 **/
@Slf4j
@Component
@Aspect
@Order(-1)
public class JobRunCallAspect {
    
    @Autowired
    private JobRunCallBack jobRunCallBack;
    
    @Around("@annotation(jobCall)")
    public Object jobAround(ProceedingJoinPoint jointPoint, JobCall jobCall){
        setJobParams();
        if (jobCall.jobRunType() == JobRunType.SYNC_RUN) {
            return runJobCallBack(jointPoint);
        }else {
            BusinessThreadPool.execute(() -> {
                runJobCallBack(jointPoint);
            });
            return null;
        }
    }
    
    public void setJobParams(){
        try {
            Optional<HttpServletRequest> requestOptional = Optional.ofNullable(RequestContextHolder.getRequestAttributes()).map(requestAttributes -> ((ServletRequestAttributes) requestAttributes)).map(ServletRequestAttributes::getRequest);
            requestOptional.ifPresent(request -> {
                BaseParameterHolder.setParameter(JOB_INFO_ID,request.getHeader(JOB_INFO_ID));
                BaseParameterHolder.setParameter(JOB_RUN_RECORD_ID,request.getHeader(JOB_RUN_RECORD_ID));
            });
        }catch (Exception e) {
            log.error("setJobParams error",e);
        }
    }
    
    public Object runJobCallBack(ProceedingJoinPoint jointPoint){
        Object result = null;
        try {
            result = jointPoint.proceed();
            jobRunCallBack.callBack(JobRunStatus.RUN_SUCCESS.getMsg(), JobRunStatus.RUN_SUCCESS.getCode());
        }catch (Throwable e) {
            log.warn("jobAround error",e);
            jobRunCallBack.callBack(e.toString(), JobRunStatus.RUN_SUCCESS.getCode());
        }
        return result;
    }
}
