package com.example.notice.config;

import lombok.Data;

import java.util.List;

@Data
public class PlatformAddressProperties {

    /**
     * 钉钉
     */
    public String dingDingWebhook;

    /**
     * 企业微信
     */
    public String weComWebhook;
    
    /**
     * 飞书
     */
    public String feiShuWebhook;

    /**
     * 邮件
     */
    private List<String> emails;
}
