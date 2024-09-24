package com.damai.monitor;


import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 服务监控 钉钉配置
 * @author: 阿星不是程序员
 **/
@RequiredArgsConstructor
public class DingTalkMessage {
    
    private final String token;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    private HttpEntity<String> createMessage(String message) {
        Map<String, Object> messageJson = new HashMap<>(8);
        Map<String, Object> context = new HashMap<>(8);
        context.put("content", message);
        messageJson.put("text", JSON.toJSONString(context));
        messageJson.put("msgtype", "text");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(JSON.toJSONString(messageJson), headers);
    }
    
    public void sendMessage(String message){
        if (StringUtils.isNotEmpty(token)) {
            restTemplate.postForEntity(token, createMessage(message), Void.class);
        }
    }
}
