package com.damai.request;

import com.damai.util.StringUtil;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: request包装
 * @author: 阿星不是程序员
 **/
public class CustomizeRequestWrapper extends HttpServletRequestWrapper {
    
    private final String requestBody;
        
    
    public CustomizeRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody = StringUtil.inputStreamConvertString(request.getInputStream());
    }
    
    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = 
                new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
            
            @Override
            public boolean isFinished() {
                return false;
            }
            
            @Override
            public boolean isReady() {
                return false;
            }
            
            @Override
            public void setReadListener(ReadListener listener) {
                
            }
        };
    }
    
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
    
    public String getRequestBody() {
        return this.requestBody;
    }
}
