package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job类型
 * @author: 阿星不是程序员
 **/
public enum JobInfoMethodCode {
    /**
     * job类型
     * */
    GET(1, "get方法"),
    
    POST(2,"post方法"),
    
    PUT(3,"put方法");
    
    private Integer code;
    
    private String msg = "";
    
    JobInfoMethodCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public String getMsg() {
        return this.msg == null ? "" : this.msg;
    }
    
    public static String getMsg(Integer code) {
        for (JobInfoMethodCode re : JobInfoMethodCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }
    
    public static JobInfoMethodCode getRc(Integer code) {
        for (JobInfoMethodCode re : JobInfoMethodCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
