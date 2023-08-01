package com.example.agent.plugin.thread.enhance;


import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ServiceRequestEnhanceOperation {
    public static RequestAttributes enhanceRequestAttributes(RequestAttributes requestAttributes) {
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        return new ServletRequestAttributes(new ServiceRequestEnhance(request));
    }
}