package com.example.threadpool.filter;


import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: msa-common
 * @description: 将requestId放入slf4j的MDC,用于后续获取
 * @author: lk
 * @create: 2021-11-08 16:48
 **/
public class RequestContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest msaRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (msaRequest != null) {
                String requestId = msaRequest.getHeader("requestId");
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
        }else {
            filterChain.doFilter(request, response);
        }

    }
}
