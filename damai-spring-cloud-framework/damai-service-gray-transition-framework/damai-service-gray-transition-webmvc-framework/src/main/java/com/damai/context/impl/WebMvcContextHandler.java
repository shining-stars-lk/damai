package com.damai.context.impl;

import com.damai.context.ContextHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: WebMvc上下文获取实现
 * @author: 阿星不是程序员
 **/
public class WebMvcContextHandler implements ContextHandler {
    @Override
    public String getValueFromHeader(final String name) {
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.nonNull(request)) {
            return request.getHeader(name);
        }
        return null;
    }
    
    public HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = getRestAttributes();
        if (attributes == null) {
            return null;
        }
        
        return attributes.getRequest();
    }
    
    public ServletRequestAttributes getRestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return (ServletRequestAttributes) requestAttributes;
    }
}
