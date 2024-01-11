package com.example.enums;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/
public enum BaseCode {
    
    SUCCESS(0, "OK"),
    SYSTEM_ERROR(-1,"系统异常"),
    
    USER_EXIST(600,"用户已存在"),
    
    USER_LOG_IN_STATUS_ERROR(601,"用户不是登录状态"),
    
    USER_LOG_IN(602,"用户已登录"),
    
    USER_ID_EMPTY(603,"用户id为空"),
    
    USER_EMPTY(604,"用户不存在"),
    
    TICKET_USER_EXIST(605,"此购票人已存在"),
    
    TICKET_USER_EMPTY(606,"此购票人不存在"),
    
    NOT_FOUND(404,"not found api %s %s"),
    
    GENERATE_RSA_SIGN_ERROR(999,"生成res签名验证失败"),
    RSA_SIGN_ERROR(1000,"res签名验证失败"),
    
    RSA_DECRYPT_ERROR(1001,"res解密失败"),
    
    RSA_ENCRYPT_ERROR(1002,"res加密失败"),
    
    AES_ERROR(1003,"aes验证失败"),
    
    CUSTOM_ENABLED_RULE_EMPTY(1004,"customEnabledRule为空"),
    
    I_LOAD_BALANCER_RULE_EMPTY(1005,"iLoadBalancer为空"),
    
    SERVER_LIST_EMPTY(1006,"serverList为空，请检查灰度代码或者服务是否异常下线"),
    
    CHANNEL_DATA(1050,"渠道数据为空"),
    
    ARGUMENT_EMPTY(1051,"基础参数为空"),
    
    HEAD_ARGUMENT_EMPTY(1052,"请求头基础参数为空"),
    
    CODE_EMPTY(1053,"code参数为空"),
    
    PARAMETER_ERROR(1054,"参数验证异常"),
    
    TOKEN_EXPIRE(1055,"token过期"),
    
    API_RULE_TRIGGER(1056,"用户调用太过频繁，稍后再试"),
    
    API_RULE_TIME_WINDOW_INTERSECT(1057,"已有的时间范围已经包含"),
    
    SUBMIT_FREQUENT(2000,"执行频繁，请稍后再试"),
    
    DEPARTMENT_STRATEGY(3002,"departmentStrategy策略实现未找到"),
    
    PRODUCT_STOCK_NOT_ENOUGH(3003,"库存不足"),
    
    JOB_INFO_NOT_EXIST(3004,"jobInfo不存在"),
    
    MESSAGE_PLATFORM_NOT_EXIST(3005,"消息平台不存在"),
    
    GENERATE_STRATEGY_NOT_EXIST(3006,"执行的生成策略不存在"),
    
    REJECT_STRATEGY_NOT_EXIST(3007,"执行的拒绝策略不存在"),
    
    PROGRAM_NOT_EXIST(4000,"节目不存在")
    ;
    
    private final Integer code;
    
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
