package com.damai.notice.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
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
public class WeComPlatform extends Platform {
    
    public WeComPlatform(final ApiStatNoticeProperties apiStatNoticeProperties) {
        super(apiStatNoticeProperties);
    }
    
    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/91770">...</a>
     * */
    @Override
    public void sendRelMessage(final String content) {
        String weComWebhook = Optional.ofNullable(apiStatNoticeProperties.getPlatformAddress()).map(PlatformAddressProperties::getWeComWebhook).orElse(null);
        if (StringUtil.isEmpty(weComWebhook)) {
            return;
        }
        log.info("weCom send message : {}",content);
        JSONObject message = new JSONObject();
        message.put("msgtype","text");
        JSONObject text = new JSONObject();
        text.put("content",content);
        JSONArray mentionedList = new JSONArray();
        mentionedList.add("@all");
        text.put("mentioned_list",mentionedList);
        message.put("text",text);
        String result = HttpUtil.post(weComWebhook,message.toJSONString());
        log.info("weCom send message result : {}",result);
    }
}
