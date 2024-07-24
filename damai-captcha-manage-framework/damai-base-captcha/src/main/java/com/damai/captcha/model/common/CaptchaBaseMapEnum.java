package com.damai.captcha.model.common;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 底图类型枚举
 * @author: 阿星不是程序员
 **/
public enum CaptchaBaseMapEnum {
    /**
     * 枚举
     * */
    ROTATE("ROTATE", "旋转拼图底图"),
    ROTATE_BLOCK("ROTATE_BLOCK", "旋转拼图旋转块底图"),
    ORIGINAL("ORIGINAL", "滑动拼图底图"),
    SLIDING_BLOCK("SLIDING_BLOCK", "滑动拼图滑块底图"),
    PIC_CLICK("PIC_CLICK", "文字点选底图");

    private String codeValue;
    private String codeDesc;

    private CaptchaBaseMapEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public String getCodeValue() {
        return this.codeValue;
    }

    public String getCodeDesc() {
        return this.codeDesc;
    }

    /**
     * 根据codeValue获取枚举
     * */
    public static CaptchaBaseMapEnum parseFromCodeValue(String codeValue) {
        for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
            if (e.codeValue.equals(codeValue)) {
                return e;
            }
        }
        return null;
    }
    
    /**
     * 根据codeValue获取描述
     */
    public static String getCodeDescByCodeBalue(String codeValue) {
        CaptchaBaseMapEnum enumItem = parseFromCodeValue(codeValue);
        return enumItem == null ? "" : enumItem.getCodeDesc();
    }
    
    /**
     * 验证codeValue是否有效
     */
    public static boolean validateCodeValue(String codeValue) {
        return parseFromCodeValue(codeValue) != null;
    }
    
    /**
     * 列出所有值字符串
     */
    public static String getString() {
        StringBuffer buffer = new StringBuffer();
        for (CaptchaBaseMapEnum e : CaptchaBaseMapEnum.values()) {
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }

}
