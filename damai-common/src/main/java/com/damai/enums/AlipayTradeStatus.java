package com.damai.enums;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付宝相关信息
 * @author: 阿星不是程序员
 **/
public enum AlipayTradeStatus {
    /**
     * 支付宝相关信息
     * */
    WAIT_BUYER_PAY(1,"wait_buyer_pay","交易创建，等待买家付款"),
    
    TRADE_CLOSED(2,"trade_closed","未付款交易超时关闭，或支付完成后全额退款"),
    
    TRADE_SUCCESS(3,"TRADE_SUCCESS","交易支付成功"),
    
    TRADE_FINISHED(4,"TRADE_FINISHED","交易结束，不可退款"),
    ;

    private Integer code;
    
    private String value;

    private String msg;

    AlipayTradeStatus(Integer code, String value, String msg) {
        this.code = code;
        this.value = value;
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
    
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public static String getMsg(Integer code) {
        for (AlipayTradeStatus re : AlipayTradeStatus.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static AlipayTradeStatus getRc(Integer code) {
        for (AlipayTradeStatus re : AlipayTradeStatus.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
