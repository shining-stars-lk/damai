package com.example.filter;


import com.example.core.StringUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.constant.Constant.TRACE_ID;

/**
 * @program: common
 * @description: 将requestId放入slf4j的MDC,用于后续获取
 * @author: k
 * @create: 2021-11-08 16:48
 **/
@Component
public class RequestContextFilter extends OncePerRequestFilter {
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
