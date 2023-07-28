package com.example.filter;


import com.example.core.StringUtil;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.constant.Constant.TRACE_ID;

/**
 * @program: cook-frame
 * @description: 
 * @author: 星哥
 * @create: 2022-11-08 16:48
 **/

public class RequestParamContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request != null) {
            String traceId = request.getHeader(TRACE_ID);
            if (StringUtil.isNotEmpty(traceId)){
                MDC.put(TRACE_ID,traceId);
            }
            try {
                filterChain.doFilter(request, response);
            }finally {
                MDC.remove(TRACE_ID);
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
}
