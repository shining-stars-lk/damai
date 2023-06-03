package com.example.service;

import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import com.example.property.GatewayProperty;
import com.example.redis.DistributCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-03
 **/
@Component
@Slf4j
public class ApiLimitService {
    
    @Autowired
    private DistributCache distributCache;
    
    @Autowired
    private GatewayProperty gatewayProperty;
    
    private DefaultRedisScript redisScript;
    
    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/apiLimit.lua")));
            redisScript.setResultType(Long.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public boolean checkApiLimit(String requestURI){
        if (gatewayProperty.getApiLimitPaths() != null && gatewayProperty.getApiLimitPaths().length > 0) {
            for (String apiLimitPath : gatewayProperty.getApiLimitPaths()) {
                PathMatcher matcher = new AntPathMatcher();
                if(matcher.match(apiLimitPath, requestURI)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void apiLimit(String id, String requestPath, ServerHttpRequest request){
        if (checkApiLimit(requestPath)) {
            Long count = 0L;
            try {
                String ip = getIpAddress(request);
                String key = "apiLimit" + "_" + ip;
                if (StringUtil.isNotEmpty(id)) {
                    key = key + "_" + id;
                }
                key = key + "_" + requestPath;
                List<String> keyList = new ArrayList<>();
                keyList.add(key);
                count = (Long) distributCache.getInstance().execute(redisScript, keyList,gatewayProperty.getIncrementValue(),gatewayProperty.getExpirationTime());
                log.info("apiLimit key值 : {}  count值 : {}",key, count);
            } catch (Exception ex) {
                log.error("redis Lua eror", ex);
            }
            if (count > gatewayProperty.getSemaphore()) {
                throw new ToolkitException(BaseCode.SUBMIT_FREQUENT);
            }
        }
    }
    
    /**
     * 获取请求的归属IP地址
     *
     * @param request 请求
     * @return
     */
    private String getIpAddress(ServerHttpRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeaders().getFirst("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeaders().getFirst("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeaders().getFirst("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddress().toString();
            }
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
