package com.damai.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.core.RedisKeyManage;
import com.damai.util.StringUtil;
import com.damai.dto.ApiDataDto;
import com.damai.enums.ApiRuleType;
import com.damai.enums.BaseCode;
import com.damai.enums.RuleTimeUnit;
import com.damai.exception.DaMaiFrameException;
import com.damai.kafka.ApiDataMessageSend;
import com.damai.property.GatewayProperty;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.lua.ApiRestrictCacheOperate;
import com.damai.util.DateUtils;
import com.damai.vo.DepthRuleVo;
import com.damai.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 接口请求记录
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class ApiRestrictService {
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private GatewayProperty gatewayProperty;
    
    @Autowired(required = false)
    private ApiDataMessageSend apiDataMessageSend;
    
    @Autowired
    private ApiRestrictCacheOperate apiRestrictCacheOperate;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    public boolean checkApiRestrict(String requestUri){
        if (gatewayProperty.getApiRestrictPaths() != null) {
            for (String apiRestrictPath : gatewayProperty.getApiRestrictPaths()) {
                PathMatcher matcher = new AntPathMatcher();
                if(matcher.match(apiRestrictPath, requestUri)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void apiRestrict(String id, String url, ServerHttpRequest request) {
        if (checkApiRestrict(url)) {
            long triggerResult = 0L;
            long triggerCallStat = 0L;
            long apiCount;
            long threshold;
            long messageIndex;
            String message = "";
            
            String ip = getIpAddress(request);
            
            StringBuilder stringBuilder = new StringBuilder(ip);
            if (StringUtil.isNotEmpty(id)) {
                stringBuilder.append("_").append(id);
            }
            String commonKey = stringBuilder.append("_").append(url).toString();
            try {
                List<DepthRuleVo> depthRuleVoList = new ArrayList<>();
          
                RuleVo ruleVo = redisCache.getForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.ALL_RULE_HASH), RedisKeyBuild.createRedisKey(RedisKeyManage.RULE).getRelKey(),RuleVo.class);
  
                String depthRuleStr = redisCache.getForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.ALL_RULE_HASH), RedisKeyBuild.createRedisKey(RedisKeyManage.DEPTH_RULE).getRelKey(),String.class);
                if (StringUtil.isNotEmpty(depthRuleStr)) {
                    depthRuleVoList = JSON.parseArray(depthRuleStr,DepthRuleVo.class);
                }
             
                int apiRuleType = ApiRuleType.NO_RULE.getCode();
                if (Optional.ofNullable(ruleVo).isPresent()) {
                    apiRuleType = ApiRuleType.RULE.getCode();
                    message = ruleVo.getMessage();
                }
                if (Optional.ofNullable(ruleVo).isPresent() && CollectionUtil.isNotEmpty(depthRuleVoList)) {
                    apiRuleType = ApiRuleType.DEPTH_RULE.getCode();
                }
                if (apiRuleType == ApiRuleType.RULE.getCode() || apiRuleType == ApiRuleType.DEPTH_RULE.getCode()) {
                    
                    assert ruleVo != null;
                    JSONObject parameter = getRuleParameter(apiRuleType,commonKey,ruleVo);
                    
                    if (apiRuleType == ApiRuleType.DEPTH_RULE.getCode()) {
                        
                        parameter = getDepthRuleParameter(parameter,commonKey,depthRuleVoList);
                    }
                    ApiRestrictData apiRestrictData = apiRestrictCacheOperate
                            .apiRuleOperate(Collections.singletonList(JSON.toJSONString(parameter)), new Object[]{});
                    
                    triggerResult = apiRestrictData.getTriggerResult();
             
                    triggerCallStat = apiRestrictData.getTriggerCallStat();
                
                    apiCount = apiRestrictData.getApiCount();
                  
                    threshold = apiRestrictData.getThreshold();
             
                    messageIndex = apiRestrictData.getMessageIndex();
                    if (messageIndex != -1) {
                        message = Optional.ofNullable(depthRuleVoList.get((int)messageIndex))
                                .map(DepthRuleVo::getMessage)
                                .filter(StringUtil::isNotEmpty)
                                .orElse(message);
                    }
                    log.info("api rule [key : {}], [triggerResult : {}], [triggerCallStat : {}], [apiCount : {}], [threshold : {}]",commonKey,triggerResult,triggerCallStat,apiCount,threshold);
                }
            }catch (Exception e) {
                log.error("redis Lua eror", e);
            }
            if (triggerResult == 1) {
                if (triggerCallStat == ApiRuleType.RULE.getCode() || triggerCallStat == ApiRuleType.DEPTH_RULE.getCode()) {
                    saveApiData(request, url, (int)triggerCallStat);
                }
                String defaultMessage = BaseCode.API_RULE_TRIGGER.getMsg();
                if (StringUtil.isNotEmpty(message)) {
                    defaultMessage = message;
                }
                throw new DaMaiFrameException(BaseCode.API_RULE_TRIGGER.getCode(),defaultMessage);
            }
        }
    }
    
    public JSONObject getRuleParameter(int apiRuleType, String commonKey, RuleVo ruleVo){
        JSONObject parameter = new JSONObject();
        
        parameter.put("apiRuleType",apiRuleType);
        
        String ruleKey = "rule_api_limit" + "_" + commonKey;
        parameter.put("ruleKey",ruleKey);
        
        parameter.put("statTime",String.valueOf(Objects.equals(ruleVo.getStatTimeType(), RuleTimeUnit.SECOND.getCode()) ? ruleVo.getStatTime() : ruleVo.getStatTime() * 60));
        
        parameter.put("threshold",ruleVo.getThreshold());
        
        parameter.put("effectiveTime",String.valueOf(Objects.equals(ruleVo.getEffectiveTimeType(), RuleTimeUnit.SECOND.getCode()) ? ruleVo.getEffectiveTime() : ruleVo.getEffectiveTime() * 60));
        
        parameter.put("ruleLimitKey", RedisKeyBuild.createRedisKey(RedisKeyManage.RULE_LIMIT,commonKey).getRelKey());
        
        parameter.put("zSetRuleStatKey", RedisKeyBuild.createRedisKey(RedisKeyManage.Z_SET_RULE_STAT,commonKey).getRelKey());
        
        return parameter;
    }
    
    public JSONObject getDepthRuleParameter(JSONObject parameter,String commonKey,List<DepthRuleVo> depthRuleVoList){
        depthRuleVoList = sortStartTimeWindow(depthRuleVoList);
        
        parameter.put("depthRuleSize",String.valueOf(depthRuleVoList.size()));
        
        parameter.put("currentTime",System.currentTimeMillis());
        
        List<JSONObject> depthRules = new ArrayList<>();
        for (int i = 0; i < depthRuleVoList.size(); i++) {
            JSONObject depthRule = new JSONObject();
            DepthRuleVo depthRuleVo = depthRuleVoList.get(i);
            
            depthRule.put("statTime",Objects.equals(depthRuleVo.getStatTimeType(), RuleTimeUnit.SECOND.getCode()) ? depthRuleVo.getStatTime() : depthRuleVo.getStatTime() * 60);
            
            depthRule.put("threshold",depthRuleVo.getThreshold());
            
            depthRule.put("effectiveTime",String.valueOf(Objects.equals(depthRuleVo.getEffectiveTimeType(), RuleTimeUnit.SECOND.getCode()) ? depthRuleVo.getEffectiveTime() : depthRuleVo.getEffectiveTime() * 60));
            
            depthRule.put("depthRuleLimit", RedisKeyBuild.createRedisKey(RedisKeyManage.DEPTH_RULE_LIMIT,i,commonKey).getRelKey());
            
            depthRule.put("startTimeWindowTimestamp",depthRuleVo.getStartTimeWindowTimestamp());
            depthRule.put("endTimeWindowTimestamp",depthRuleVo.getEndTimeWindowTimestamp());
            
            depthRules.add(depthRule);
        }
        
        parameter.put("depthRules",depthRules);
        
        return parameter;
    }
    
    public List<DepthRuleVo> sortStartTimeWindow(List<DepthRuleVo> depthRuleVoList){
        return depthRuleVoList.stream().peek(depthRuleVo -> {
            depthRuleVo.setStartTimeWindowTimestamp(getTimeWindowTimestamp(depthRuleVo.getStartTimeWindow()));
            depthRuleVo.setEndTimeWindowTimestamp((getTimeWindowTimestamp(depthRuleVo.getEndTimeWindow())));
        }).sorted(Comparator.comparing(DepthRuleVo::getStartTimeWindowTimestamp)).collect(Collectors.toList());
    }
    
    public long getTimeWindowTimestamp(String timeWindow){
        String today = DateUtil.today();
        return DateUtil.parse(today + " " + timeWindow).getTime();
    }
    
    /**
      * 获取请求的归属IP地址
      *
      * @param request 请求
      */
    public static String getIpAddress(ServerHttpRequest request) {
        String unknown = "unknown";
        String split = ",";
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !unknown.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(split)) {
                ip = ip.split(split)[0];
            }
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        }
        return ip;
    }
    
    public void saveApiData(ServerHttpRequest request, String apiUrl, Integer type){
        ApiDataDto apiDataDto = new ApiDataDto();
        apiDataDto.setId(uidGenerator.getUid());
        apiDataDto.setApiAddress(getIpAddress(request));
        apiDataDto.setApiUrl(apiUrl);
        apiDataDto.setCreateTime(DateUtils.now());
        apiDataDto.setCallDayTime(DateUtils.nowStr(DateUtils.FORMAT_DATE));
        apiDataDto.setCallHourTime(DateUtils.nowStr(DateUtils.FORMAT_HOUR));
        apiDataDto.setCallMinuteTime(DateUtils.nowStr(DateUtils.FORMAT_MINUTE));
        apiDataDto.setCallSecondTime(DateUtils.nowStr(DateUtils.FORMAT_SECOND));
        apiDataDto.setType(type);
        Optional.ofNullable(apiDataMessageSend).ifPresent(send -> send.sendMessage(JSON.toJSONString(apiDataDto)));
    }
}
