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
public class FeiShuPlatform extends Platform {
    
    public FeiShuPlatform(final ApiStatNoticeProperties apiStatNoticeProperties) {
        super(apiStatNoticeProperties);
    }
    
    /**
     * <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot">...</a>
     * */
    @Override
    public void sendRelMessage(final String content) {
        String feiShuWebhook = Optional.ofNullable(apiStatNoticeProperties.getPlatformAddress()).map(PlatformAddressProperties::getFeiShuWebhook).orElse(null);
        if (StringUtil.isEmpty(feiShuWebhook)) {
            return;
        }
        log.info("feiShu send message : {}",content);
        JSONObject message = new JSONObject();
        message.put("msg_type","text");
        JSONObject contentJson = new JSONObject();
        contentJson.put("text",content+"<at user_id=\"all\">所有人</at>");
        message.put("content",contentJson);
        String result = HttpUtil.post(feiShuWebhook,message.toJSONString());
        log.info("feiShu send message : {}",result);
    }
}
