package com.example.filter;

import com.example.core.StringUtil;
import com.example.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.constant.Constant.MARK_PARAMETER;
import static com.example.constant.Constant.TRACE_ID;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-05-09
 **/
@Slf4j
public class BaseParameterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        log.info("current thread doFilterInternal : {}",Thread.currentThread().getName());
        if (request != null) {
            String traceId = request.getHeader(TRACE_ID);
            String mark = request.getHeader(MARK_PARAMETER);
            try {
                if (StringUtil.isNotEmpty(traceId)) {
                    BaseParameterHolder.setParameter(TRACE_ID,traceId);
                    MDC.put(TRACE_ID,traceId);
                }
                if (StringUtil.isNotEmpty(mark)) {
                    BaseParameterHolder.setParameter(MARK_PARAMETER,mark);
                    MDC.put(MARK_PARAMETER,mark);
                }
                filterChain.doFilter(request, response);
            }finally {
                BaseParameterHolder.removeParameter(TRACE_ID);
                MDC.remove(TRACE_ID);
                BaseParameterHolder.removeParameter(MARK_PARAMETER);
                MDC.remove(MARK_PARAMETER);
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
