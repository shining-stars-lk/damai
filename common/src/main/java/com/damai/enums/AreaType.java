package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 地区枚举
 * @author: 阿宽不是程序员
 **/
public enum AreaType {
    PROVINCE(1,"省"),
    MUNICIPALITIES(2,"市"),
    
    PREFECTURE(3,"区或县"),
    ;

    private Integer code;

    private String msg;

    AreaType(Integer code, String msg) {
        this.code = code;
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

    public static String getMsg(Integer code) {
        for (AreaType re : AreaType.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static AreaType getRc(Integer code) {
        for (AreaType re : AreaType.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
