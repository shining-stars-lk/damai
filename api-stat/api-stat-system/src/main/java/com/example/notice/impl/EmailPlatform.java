package com.example.notice.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.mail.MailUtil;
import com.example.notice.Platform;
import com.example.notice.config.ApiStatNoticeProperties;
import com.example.notice.config.PlatformAddressProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@Slf4j
public class EmailPlatform extends Platform {
    
    private final String SUBJECT = "API耗时通知";
    
    public EmailPlatform(final ApiStatNoticeProperties apiStatNoticeProperties) {
        super(apiStatNoticeProperties);
    }
    
    @Override
    public void sendRelMessage(final String content) {
        List<String> emails = Optional.ofNullable(apiStatNoticeProperties.getPlatformAddress()).map(PlatformAddressProperties::getEmails).orElse(null);
        if (CollectionUtil.isEmpty(emails) || emails.size() == 0) {
            return;
        }
        log.info("email send message : {}",content);
        MailUtil.send(emails, SUBJECT, content, false);
    }
}
