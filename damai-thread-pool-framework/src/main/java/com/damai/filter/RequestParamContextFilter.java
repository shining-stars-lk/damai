package com.damai.filter;


import com.damai.util.StringUtil;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.damai.constant.Constant.TRACE_ID;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 链路过滤器
 * @author: 阿星不是程序员
 **/

public class RequestParamContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(TRACE_ID);
        if (StringUtil.isNotEmpty(traceId)){
            MDC.put(TRACE_ID,traceId);
        }
        try {
            filterChain.doFilter(request, response);
        }finally {
            MDC.remove(TRACE_ID);
        }
    }
}
