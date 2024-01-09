package com.example.enums;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/
public enum IdType {
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
