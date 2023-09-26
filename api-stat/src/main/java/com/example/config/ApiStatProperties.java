package com.example.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zhangchang
 */
@Data
@ConfigurationProperties(prefix = "api-stat")
public class ApiStatProperties {
    private Boolean enable;
    private String language;
    @Deprecated
    private String logLanguage;
    private Boolean logEnable;
    private Boolean versionNotice;
    private Double threshold;
    private Double discardRate;
    private String pointcut;
    private Boolean exceptionEnable;
    private Boolean paramAnalyse;
    private String saver;
    private String dataSource;
    private String redisTemplate;
    private Integer threadNum;
    private String contextPath;
    private String dataPrefix;
    private Boolean dataReset;
    private Boolean authEnable;
    private String userName;
    private String password;
    private String staticToken;
    private Long authExpire;
    private Boolean mailEnable;
    private String mailHost;
    private Integer mailPort;
    private String mailProtocol;
    private String mailEncoding;
    private String mailUser;
    private String mailCode;
    private String mailReceivers;
    private Integer mailThreshold;
    private String mailScope;
    private String propertyFile;

}
