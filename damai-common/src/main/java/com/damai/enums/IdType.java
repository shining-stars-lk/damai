package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 证件类型
 * @author: 阿星不是程序员
 **/
public enum IdType {
    /**
     * 证件类型
     * */
    IDENTITY(1, "身份证"),
    HK_M_TW_PERMIT(2, "港澳台居民居住证"),
    HKM_PASS(3, "港澳居民来往内地通行证"),
    TW_PASS(4, "台湾居民来往内地通行证"),
    PASSPORT(5, "护照"),
    FOREIGNER_RESIDENCE_PERMIT(6,"外国人永久居住证"),
    ;

    private Integer code;

    private String msg;

    IdType(Integer code, String msg) {
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
        for (IdType re : IdType.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static IdType getRc(Integer code) {
        for (IdType re : IdType.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
