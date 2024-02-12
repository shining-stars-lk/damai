package com.example.enums;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/
public enum TimeType {
    WEEK(1,"本周内"),
    MONTH(2,"一个月内"),
    ;

    private Integer code;

    private String msg;

    TimeType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }
    
    public String getMsg() {
        return this.msg == null ? "" : this.msg;
    }
    
    public static String getMsg(Integer code) {
        for (TimeType re : TimeType.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static TimeType getRc(Integer code) {
        for (TimeType re : TimeType.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
