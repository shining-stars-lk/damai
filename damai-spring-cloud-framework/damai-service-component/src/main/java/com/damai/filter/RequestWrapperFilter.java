package com.damai.filter;

import com.damai.request.CustomizeRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: request包装过滤器
 * @author: 阿星不是程序员
 **/
public class RequestWrapperFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, 
                                    final FilterChain filterChain) throws ServletException, IOException {
        CustomizeRequestWrapper requestWrapper = new CustomizeRequestWrapper(request);
        filterChain.doFilter(requestWrapper,response);
    }
}
