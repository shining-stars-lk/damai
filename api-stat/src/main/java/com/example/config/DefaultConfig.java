package com.example.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zhangchang
 */
@Component
@ConfigurationProperties(prefix = "ko-time")
public class DefaultConfig {
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

    public String getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public Boolean getMailEnable() {
        return mailEnable;
    }

    public void setMailEnable(Boolean mailEnable) {
        this.mailEnable = mailEnable;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public Integer getMailPort() {
        return mailPort;
    }

    public void setMailPort(Integer mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailProtocol() {
        return mailProtocol;
    }

    public void setMailProtocol(String mailProtocol) {
        this.mailProtocol = mailProtocol;
    }

    public String getMailEncoding() {
        return mailEncoding;
    }

    public void setMailEncoding(String mailEncoding) {
        this.mailEncoding = mailEncoding;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailCode() {
        return mailCode;
    }

    public void setMailCode(String mailCode) {
        this.mailCode = mailCode;
    }

    public String getMailReceivers() {
        return mailReceivers;
    }

    public void setMailReceivers(String mailReceivers) {
        this.mailReceivers = mailReceivers;
    }

    public Integer getMailThreshold() {
        return mailThreshold;
    }

    public void setMailThreshold(Integer mailThreshold) {
        this.mailThreshold = mailThreshold;
    }

    public String getMailScope() {
        return mailScope;
    }

    public void setMailScope(String mailScope) {
        this.mailScope = mailScope;
    }

    public String getStaticToken() {
        return staticToken;
    }

    public void setStaticToken(String staticToken) {
        this.staticToken = staticToken;
    }

    public Boolean getVersionNotice() {
        return versionNotice;
    }

    public void setVersionNotice(Boolean versionNotice) {
        this.versionNotice = versionNotice;
    }

    public Long getAuthExpire() {
        return authExpire;
    }

    public void setAuthExpire(Long authExpire) {
        this.authExpire = authExpire;
    }

    public Double getDiscardRate() {
        return discardRate;
    }

    public void setDiscardRate(Double discardRate) {
        this.discardRate = discardRate;
    }

    public Boolean getDataReset() {
        return dataReset;
    }

    public void setDataReset(Boolean dataReset) {
        this.dataReset = dataReset;
    }

    public String getDataPrefix() {
        return dataPrefix;
    }

    public void setDataPrefix(String dataPrefix) {
        this.dataPrefix = dataPrefix;
    }

    public String getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(String redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getLogLanguage() {
        return logLanguage;
    }

    public void setLogLanguage(String logLanguage) {
        this.logLanguage = logLanguage;
    }

    public Boolean getLogEnable() {
        return logEnable;
    }

    public void setLogEnable(Boolean logEnable) {
        this.logEnable = logEnable;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public Boolean getExceptionEnable() {
        return exceptionEnable;
    }

    public void setExceptionEnable(Boolean exceptionEnable) {
        this.exceptionEnable = exceptionEnable;
    }

    public String getSaver() {
        return saver;
    }

    public void setSaver(String saver) {
        this.saver = saver;
    }


    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAuthEnable() {
        return authEnable;
    }

    public void setAuthEnable(Boolean authEnable) {
        this.authEnable = authEnable;
    }

    public Boolean getParamAnalyse() {
        return paramAnalyse;
    }

    public void setParamAnalyse(Boolean paramAnalyse) {
        this.paramAnalyse = paramAnalyse;
    }

}
