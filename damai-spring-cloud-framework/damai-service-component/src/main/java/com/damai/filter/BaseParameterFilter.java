package com.damai.filter;

import com.damai.threadlocal.BaseParameterHolder;
import com.damai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.damai.constant.Constant.CODE;
import static com.damai.constant.Constant.GRAY_PARAMETER;
import static com.damai.constant.Constant.TRACE_ID;
import static com.damai.constant.Constant.USER_ID;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 业务服务过滤器
 * @author: 阿星不是程序员
 **/
@Slf4j
public class BaseParameterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        ServletInputStream sis = request.getInputStream();
        String requestBody  = StringUtil.inputStreamConvertString(sis);
        if (StringUtil.isNotEmpty(requestBody)) {
            requestBody = requestBody.replaceAll(" ", "").replaceAll("\r\n","");
        }
        String traceId = request.getHeader(TRACE_ID);
        String gray = request.getHeader(GRAY_PARAMETER);
        String userId = request.getHeader(USER_ID);
        String code = request.getHeader(CODE);
        
        try {
            if (StringUtil.isNotEmpty(traceId)) {
                    BaseParameterHolder.setParameter(TRACE_ID,traceId);
                MDC.put(TRACE_ID,traceId);
            }
            if (StringUtil.isNotEmpty(gray)) {
                BaseParameterHolder.setParameter(GRAY_PARAMETER,gray);
                MDC.put(GRAY_PARAMETER,gray);
            }
            if (StringUtil.isNotEmpty(userId)) {
                BaseParameterHolder.setParameter(USER_ID,userId);
                MDC.put(USER_ID,userId);
            }
            if (StringUtil.isNotEmpty(code)) {
                BaseParameterHolder.setParameter(CODE,code);
                MDC.put(CODE,code);
            }
            log.info("current api : {} requestBody : {}",request.getRequestURI(), requestBody);
            filterChain.doFilter(request, response);
        }finally {
            BaseParameterHolder.removeParameter(TRACE_ID);
            MDC.remove(TRACE_ID);
            BaseParameterHolder.removeParameter(GRAY_PARAMETER);
            MDC.remove(GRAY_PARAMETER);
            BaseParameterHolder.removeParameter(USER_ID);
            MDC.remove(USER_ID);
            BaseParameterHolder.removeParameter(CODE);
            MDC.remove(CODE);
        }
    }
}
