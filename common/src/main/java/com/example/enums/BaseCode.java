package com.example.enums;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-03
 **/
public enum BaseCode {
    
    SUCCESS(0, "OK"),
    SYSTEM_ERROR(-1,"系统异常"),
    RSA_SIGN_ERROR(1,"res签名验证失败"),
    
    AES_ERROR(2,"aes验证失败"),
    
    CHANNEL_DATA(3,"渠道数据为空"),
    
    ARGUMENT_EMPTY(4,"基础参数为空"),
    
    SUBMIT_FREQUENT(5,"执行频繁，请稍后再试"),
    
    USER_ID_EMPTY(6,"用户id为空"),
    
    USER_EMPTY(7,"用户为空"),
    
    CODE_EMPTY(8,"code参数为空"),
    
    DEPARTMENT_STRATEGY(9,"departmentStrategy策略实现未找到"),
    
    PARAMETER_ERROR(10,"参数验证异常"),
    
    PRODUCT_STOCK_NOT_ENOUGH(11,"库存不足"),
    
    JOB_INFO_NOT_EXIST(12,"jobInfo不存在");
    
    private Integer code;
    
    private String msg = "";
    
    BaseCode(Integer code, String msg) {
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
        for (BaseCode re : BaseCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }
    
    public static BaseCode getRc(Integer code) {
        for (BaseCode re : BaseCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
