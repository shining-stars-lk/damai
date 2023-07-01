package com.example.service;

import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.example.core.CacheKeyEnum;
import com.example.core.StringUtil;
import com.example.dto.ApiDataDto;
import com.example.enums.BaseCode;
import com.example.enums.RuleTimeUnit;
import com.example.exception.ToolkitException;
import com.example.kafka.ApiDataMessageSend;
import com.example.property.GatewayProperty;
import com.example.redis.CacheKeyWrap;
import com.example.redis.DistributCache;
import com.example.util.DateUtils;
import com.example.vo.RuleVo;
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
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-03
 **/
@Slf4j
@Component
public class ApiRestrictService {
    
    @Autowired
    private DistributCache distributCache;
    
    @Autowired
    private GatewayProperty gatewayProperty;
    
    @Autowired(required = false)
    private ApiDataMessageSend apiDataMessageSend;
    
    @Resource
    private UidGenerator uidGenerator;
    
    private DefaultRedisScript redisScript;
    
    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/apiLimit.lua")));
            redisScript.setResultType(List.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public boolean checkApiRestrict(String requestURI){
        if (gatewayProperty.getApiRestrictPaths() != null && gatewayProperty.getApiRestrictPaths().length > 0) {
            for (String apiRestrictPath : gatewayProperty.getApiRestrictPaths()) {
                PathMatcher matcher = new AntPathMatcher();
                if(matcher.match(apiRestrictPath, requestURI)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void apiRestrict(String id, String uri, ServerHttpRequest request){
        if (checkApiRestrict(uri)) {
            long result = 0L;
            long count = 0L;
            long thresholdValue = 0L;
            String message = "";
            try {
                RuleVo ruleVo = distributCache.get(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.RULE), RuleVo.class);
                if (Optional.ofNullable(ruleVo).isPresent()) {
                    message = ruleVo.getMessage();
                    String ip = getIpAddress(request);
                    StringBuffer stringBuffer = new StringBuffer("apiLimit" + "_" + ip);
                    if (StringUtil.isNotEmpty(id)) {
                        stringBuffer.append("_").append(id);
                    }
                    String key = stringBuffer.append("_").append(uri).toString();
                    List<String> keyList = new ArrayList<>();
                    keyList.add(key);
                    keyList.add(String.valueOf(ruleVo.getStatisticTimeType() == RuleTimeUnit.SECOND.getCode() ? ruleVo.getStatisticTime() : ruleVo.getStatisticTime() * 60));
                    keyList.add(String.valueOf(ruleVo.getThresholdValue()));
                    keyList.add(String.valueOf(ruleVo.getLimitTimeType() == RuleTimeUnit.SECOND.getCode() ? ruleVo.getLimitTime() : ruleVo.getLimitTime() * 60));
                    keyList.add(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.RULE_LIMIT,key).getRelKey());
                    List<Long> executeResultList = (ArrayList) distributCache.getInstance().execute(redisScript, keyList);
                    result = executeResultList.get(0);
                    count = executeResultList.get(1);
                    thresholdValue = executeResultList.get(2);
                    log.info("apiLimit key : {} result : {} count : {} thresholdValue : {}",key, result, count, thresholdValue);
                }
            } catch (Exception ex) {
                log.error("redis Lua eror", ex);
            }
            if (result == 1) {
                if (count == thresholdValue) {
                    saveApiData(request,uri);
                }
                if (StringUtil.isNotEmpty(message)) {
                    throw new ToolkitException(BaseCode.SUBMIT_FREQUENT.getCode(),message);
                }else {
                    throw new ToolkitException(BaseCode.SUBMIT_FREQUENT);
                }
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
    
    public void saveApiData(ServerHttpRequest request, String apiUrl){
        ApiDataDto apiDataDto = new ApiDataDto();
        apiDataDto.setId(String.valueOf(uidGenerator.getUID()));
        apiDataDto.setApiAddress(getIpAddress(request));
        apiDataDto.setApiUrl(apiUrl);
        apiDataDto.setCreateTime(DateUtils.now());
        apiDataDto.setCallDayTime(DateUtils.nowStr(DateUtils.FORMAT_DATE));
        apiDataDto.setCallHourTime(DateUtils.nowStr(DateUtils.FORMAT_HOUR));
        apiDataDto.setCallMinuteTime(DateUtils.nowStr(DateUtils.FORMAT_MINUTE));
        apiDataDto.setCallMinuteTime(DateUtils.nowStr(DateUtils.FORMAT_SECOND));
        Optional.ofNullable(apiDataMessageSend).ifPresent(send -> send.sendMessage(JSON.toJSONString(apiDataMessageSend)));
    }
}
