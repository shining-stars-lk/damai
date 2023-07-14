package com.example.enums;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-03
 **/
public enum BaseCode {
    
    SUCCESS(0, "OK"),
    SYSTEM_ERROR(-1,"系统异常"),
    RSA_SIGN_ERROR(1000,"res签名验证失败"),
    
    AES_ERROR(1001,"aes验证失败"),
    
    CHANNEL_DATA(1002,"渠道数据为空"),
    
    ARGUMENT_EMPTY(1003,"基础参数为空"),
    
    HEAD_ARGUMENT_EMPTY(1004,"请求头基础参数为空"),
    
    CODE_EMPTY(1005,"code参数为空"),
    
    PARAMETER_ERROR(1005,"参数验证异常"),
    
    TOKEN_EXPIRE(1006,"token过期"),
    
    API_RULE_TRIGGER(1007,"用户调用太过频繁，稍后再试"),
    
    API_RULE_TIME_WINDOW_INTERSECT(1008,"已有的时间范围已经包含"),
    
    SUBMIT_FREQUENT(2000,"执行频繁，请稍后再试"),
    
    USER_ID_EMPTY(3000,"用户id为空"),
    
    USER_EMPTY(3001,"用户为空"),
    
    DEPARTMENT_STRATEGY(3002,"departmentStrategy策略实现未找到"),
    
    PRODUCT_STOCK_NOT_ENOUGH(3003,"库存不足"),
    
    JOB_INFO_NOT_EXIST(3004,"jobInfo不存在");
    
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
