package com.example.filter;


import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: common
 * @description: 将requestId放入slf4j的MDC,用于后续获取
 * @author: lk
 * @create: 2021-11-08 16:48
 **/
public class RequestContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request != null) {
            String requestId = request.getHeader("requestId");
            if (requestId == null){
                MDC.put("requestId",requestId);
            }
            try {
                filterChain.doFilter(request, response);
            }finally {
                MDC.remove("requestId");
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
}
