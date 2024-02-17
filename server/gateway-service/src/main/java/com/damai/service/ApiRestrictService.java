package com.damai.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.core.RedisKeyEnum;
import com.damai.core.StringUtil;
import com.damai.dto.ApiDataDto;
import com.damai.enums.BaseCode;
import com.damai.enums.RuleTimeUnit;
import com.damai.exception.DaMaiFrameException;
import com.damai.kafka.ApiDataMessageSend;
import com.damai.property.GatewayProperty;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
import com.damai.util.DateUtils;
import com.damai.vo.DepthRuleVo;
import com.damai.vo.RuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
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
    
    @Resource
    private UidGenerator uidGenerator;
    
    private DefaultRedisScript redisScript;
    
    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript<>();
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
    
    public void apiRestrict(String id, String url, ServerHttpRequest request) {
        if (checkApiRestrict(url)) {
            long triggerResult = 0L;
            long triggerCallStat = 0L;
            long apiCount = 0L;
            long threshold = 0L;
            long messageIndex = -1L;
            String message = "";
            
            String ip = getIpAddress(request);
            
            StringBuilder stringBuilder = new StringBuilder(ip);
            if (StringUtil.isNotEmpty(id)) {
                stringBuilder.append("_").append(id);
            }
            String commonKey = stringBuilder.append("_").append(url).toString();
            try {
                List<DepthRuleVo> depthRuleVoList = new ArrayList<>();
          
                RuleVo ruleVo = redisCache.getForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.ALL_RULE_HASH),RedisKeyWrap.createRedisKey(RedisKeyEnum.RULE).getRelKey(),RuleVo.class);
  
                String depthRuleStr = redisCache.getForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.ALL_RULE_HASH),RedisKeyWrap.createRedisKey(RedisKeyEnum.DEPTH_RULE).getRelKey(),String.class);
                if (StringUtil.isNotEmpty(depthRuleStr)) {
                    depthRuleVoList = JSON.parseArray(depthRuleStr,DepthRuleVo.class);
                }
             
                int apiRuleType = 0;
                if (Optional.ofNullable(ruleVo).isPresent()) {
                    apiRuleType = 1;
                    message = ruleVo.getMessage();
                }
                if (Optional.ofNullable(ruleVo).isPresent() && depthRuleVoList.size() > 0) {
                    apiRuleType = 2;
                }
                if (apiRuleType == 1 || apiRuleType == 2) {
                    JSONObject parameter = new JSONObject();
                    
                    parameter.put("apiRuleType",apiRuleType);
                    
                    String ruleKey = "rule_api_limit" + "_" + commonKey;
                    parameter.put("ruleKey",ruleKey);
              
                    parameter.put("statTime",String.valueOf(Objects.equals(ruleVo.getStatTimeType(), RuleTimeUnit.SECOND.getCode()) ? ruleVo.getStatTime() : ruleVo.getStatTime() * 60));
                    
                    parameter.put("threshold",ruleVo.getThreshold());
               
                    parameter.put("effectiveTime",String.valueOf(Objects.equals(ruleVo.getEffectiveTimeType(), RuleTimeUnit.SECOND.getCode()) ? ruleVo.getEffectiveTime() : ruleVo.getEffectiveTime() * 60));
                    
                    parameter.put("ruleLimitKey",RedisKeyWrap.createRedisKey(RedisKeyEnum.RULE_LIMIT,commonKey).getRelKey());
                    
                    parameter.put("zSetRuleStatKey",RedisKeyWrap.createRedisKey(RedisKeyEnum.Z_SET_RULE_STAT,commonKey).getRelKey());
                    
                    if (apiRuleType == 2) {
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
                            
                            depthRule.put("depthRuleLimit",RedisKeyWrap.createRedisKey(RedisKeyEnum.DEPTH_RULE_LIMIT,i,commonKey).getRelKey());

                            depthRule.put("startTimeWindowTimestamp",depthRuleVo.getStartTimeWindowTimestamp());
                            depthRule.put("endTimeWindowTimestamp",depthRuleVo.getEndTimeWindowTimestamp());
                            
                            depthRules.add(depthRule);
                        }
                        
                        parameter.put("depthRules",depthRules);
                    }
                    List<Long> executeResult = (ArrayList)redisCache.getInstance().execute(redisScript, Stream.of(parameter).map(p -> JSON.toJSONString(p)).collect(Collectors.toList()), new String[]{});
                
                    triggerResult = Optional.ofNullable(executeResult.get(0)).orElse(0L);
             
                    triggerCallStat = Optional.ofNullable(executeResult.get(1)).orElse(0L);
                
                    apiCount = Optional.ofNullable(executeResult.get(2)).orElse(0L);
                  
                    threshold = Optional.ofNullable(executeResult.get(3)).orElse(0L);
             
                    messageIndex = Optional.ofNullable(executeResult.get(4)).orElse(-1L);
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
                if (triggerCallStat == 1 || triggerCallStat == 2) {
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
    
    public List<DepthRuleVo> sortStartTimeWindow(List<DepthRuleVo> depthRuleVoList){
        return depthRuleVoList.stream().map(depthRuleVo -> {
            depthRuleVo.setStartTimeWindowTimestamp(getTimeWindowTimestamp(depthRuleVo.getStartTimeWindow()));
            depthRuleVo.setEndTimeWindowTimestamp((getTimeWindowTimestamp(depthRuleVo.getEndTimeWindow())));
            return depthRuleVo;
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
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
    
    public void saveApiData(ServerHttpRequest request, String apiUrl, Integer type){
        ApiDataDto apiDataDto = new ApiDataDto();
        apiDataDto.setId(uidGenerator.getUID());
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
