package com.example.handler;


import com.example.annotation.Auth;
import com.example.constant.KoConstant;
import com.example.util.Context;
import com.example.util.KoTimeNotLoginException;
import com.example.util.KoUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * zhangchang
 */
@Aspect
@Component
public class AuthHandler {
    private static Logger log = Logger.getLogger(AuthHandler.class.toString());

    @Pointcut(KoConstant.authRange)
    public void preProcess() {
    }

    @Around("preProcess()")
    public Object doAroundCompute(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        boolean needAuth = method.isAnnotationPresent(Auth.class);
        if (needAuth && Context.getConfig().getAuthEnable()) {
            if (!Context.getConfig().getEnable()) {
                throw new RuntimeException("no data to return,because kotime switch was closed!");
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getParameter("kotoken");
            if (StringUtils.hasText(token)) {
                if (StringUtils.hasText(Context.getConfig().getStaticToken())) {
                    if (!token.equals(Context.getConfig().getStaticToken()) && !KoUtil.isLogin(token)) {
                        throw new KoTimeNotLoginException("error kotoken,please let ko-time.static-token's value equals kotoken's value !");
                    }
                }else {
                    KoUtil.checkLogin(token);
                }
            }else {
                throw new KoTimeNotLoginException("can not find login information for kotime,please login first!");
            }
        }
        return pjp.proceed();
    }
}
