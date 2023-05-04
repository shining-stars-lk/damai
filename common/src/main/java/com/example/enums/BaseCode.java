package com.example.enums;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-05-04
 **/
public enum BaseCode {
    
    SUCCESS(0, "OK");
    
    private Integer code;
    private String message = "";
    
    BaseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public static String getMessage(Integer code) {
        for (BaseCode baseCode : BaseCode.values()) {
            if (baseCode.code.intValue() == code.intValue()) {
                return baseCode.message;
            }
        }
        return "";
    }
    
    public static BaseCode getResultCode(Integer code) {
        for (BaseCode baseCode : BaseCode.values()) {
            if (baseCode.code.intValue() == code.intValue()) {
                return baseCode;
            }
        }
        return null;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(final Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
