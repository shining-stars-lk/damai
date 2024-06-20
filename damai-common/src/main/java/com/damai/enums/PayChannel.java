package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付渠道
 * @author: 阿星不是程序员
 **/
public enum PayChannel {
    /**
     * 支付渠道
     * */
    ALIPAY(1,"alipay","支付宝"),
    
    WX(2,"wx","微信"),
    ;

    private Integer code;
    
    private String value;

    private String msg;

    PayChannel(Integer code, String value, String msg) {
        this.code = code;
        this.value = value;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg == null ? "" : this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public static String getMsg(Integer code) {
        for (PayChannel re : PayChannel.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static PayChannel getRc(Integer code) {
        for (PayChannel re : PayChannel.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
