package com.damai.notice.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.damai.core.StringUtil;
import com.damai.notice.config.ApiStatNoticeProperties;
import com.damai.notice.Platform;
import com.damai.notice.config.PlatformAddressProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@Slf4j
public class DingDingPlatform extends Platform {
    
    public DingDingPlatform(final ApiStatNoticeProperties apiStatNoticeProperties) {
        super(apiStatNoticeProperties);
    }
    
    /**
     * <a href="https://open.dingtalk.com/document/robots/custom-robot-access">...</a>
     * */
    @Override
    public void sendRelMessage(final String content) {
        String dingDingWebhook = Optional.ofNullable(apiStatNoticeProperties.getPlatformAddress()).map(PlatformAddressProperties::getDingDingWebhook).orElse(null);
        if (StringUtil.isEmpty(dingDingWebhook)) {
            return;
        }
        log.info("dingTalk send message : {}",content);
        JSONObject message = new JSONObject();
        message.put("msgtype","text");
        JSONObject text = new JSONObject();
        text.put("content",content);
        message.put("text",text);
        
        JSONObject at = new JSONObject();
        at.put("isAtAll",true);
        message.put("at",at);
        String result = HttpUtil.post(dingDingWebhook,message.toJSONString());
        log.info("dingTalk send message result : {}",result);
    }
}
