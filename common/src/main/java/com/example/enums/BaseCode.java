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
    
    PROGRAM_NOT_EXIST(4000,"节目不存在"),
    
    SEAT_NOT_EXIST(4001,"座位不存在"),
    
    SEAT_LOCK(4002,"座位已锁定"),
    
    SEAT_SOLD(4003,"座位已售卖"),
    
    SEAT_OCCUPY(4004,"座位已占用"),
    
    TICKET_CATEGORY_NOT_EXIST(4005,"如果不选座位，那么票档必须要存在"),
    
    TICKET_COUNT_NOT_EXIST(4006,"如果不选座位，那么购买票数量必须要存在"),
    
    TICKET_COUNT_ERROR(4007,"如果不选座位，购买票数量必须大于0"),
    
    PRICE_ERROR(4008,"入参的订单价格大于座位规定价格"),
    
    RPC_RESULT_DATA_EMPTY(4009,"rpc服务返回数据为空"),
    
    TICKET_CATEGORY_NOT_EXIST_V2(4010,"票档不存在"),
    
    TICKET_REMAIN_NUMBER_NOT_SUFFICIENT(4011,"余票数量不足"),
    
    COMPOSITE_NOT_EXIST(4012,"通用验证不存在"),
    
    USER_REGISTER_FREQUENCY(4013,"用户注册频繁"),
    
    PROGRAM_SHOW_TIME_NOT_EXIST(4014,"节目演出时间不存在"),
    
    ORDER_NOT_EXIST(4015,"订单不存在"),
    
    ORDER_CANCEL(4016,"订单已取消"),
    
    ORDER_PAY(4017,"订单已支付"),
    
    ORDER_REFUND(4018,"订单已退单"),
    
    ORDER_CANAL_ERROR(4019,"订单取消失败"),
    
    TICKET_USER_ORDER_NOT_EXIST(4019,"购票人订单不存在"),
    
    TICKET_USER_ORDER_CANCEL(4020,"购票人订单已取消"),
    
    TICKET_USER_ORDER_PAY(4021,"购票人订单已支付"),
    
    TICKET_USER_ORDER_REFUND(4022,"购票人订单已退单"),
    
    LOCK_SEAT_LIST_EMPTY(4023,"锁定的座位为空"),
    
    ORDER_EXIST(4024,"订单存在"),
    
    DELAY_QUEUE_CLIENT_NOT_EXIST(5001,"延迟队列客户端不存在"),
    
    DELAY_QUEUE_MESSAGE_NOT_EXIST(5002,"延迟队列消息不存在"),
    
    PAY_ERROR(6000,"支付异常"),
    
    PAY_STRATEGY_NOT_EXIST(6001,"此支付策略不存在"),
    
    PAY_BILL_IS_NOT_NO_PAY(6002,"此账单不是未支付状态"),
    
    ALIPAY_TRADE_STATUS_NOT_EXIST(6003,"支付宝支付状态未知"),
    
    PAY_PRICE_NOT_EQUAL_ORDER_PRICE(6004,"支付金额和订单金额不一致"),
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
