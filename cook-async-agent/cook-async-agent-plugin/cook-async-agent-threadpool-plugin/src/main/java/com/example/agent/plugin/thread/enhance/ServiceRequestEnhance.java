package com.example.agent.plugin.thread.enhance;


import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class ServiceRequestEnhance extends HttpServletRequestWrapper {
    private Map<String, List<String>> headerMap;

    public ServiceRequestEnhance(HttpServletRequest request) {
        super(request);
        
        headerMap = operateHeaders(request);
    }

    private Map<String, List<String>> operateHeaders(HttpServletRequest request) {
        // 不区分大小写的Key的Map
        Map<String, List<String>> headers = new LinkedCaseInsensitiveMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName != null) {
                headers.put(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        return headers;
    }

    @Override
    public String getHeader(String name) {
        List<String> headerValues = headerMap.get(name);

        return CollectionUtils.isEmpty(headerValues) ? null : headerValues.get(0);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> headerValues = headerMap.get(name);

        return Collections.enumeration(headerValues != null ? headerValues : Collections.emptySet());
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headerMap.keySet());
    }
}