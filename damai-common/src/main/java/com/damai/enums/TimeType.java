package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 时间类型
 * @author: 阿宽不是程序员
 **/
public enum TimeType {
    /**
     * 时间类型
     * */
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
