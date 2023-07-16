package com.tool.multiplesubmitlimit.info;

/**
 * @program: redis-tool
 * @description: 防重复提交触发策略枚举
 * @author: 星哥
 * @create: 2023-05-28
 **/
public enum MultipleSubmitLimitRejectedStrategy {

    ABORT_STRATEGY(1,"ABORT"),
    SAME_RESULT(2,"SAME_RESULT");

    private Integer code;

    private String msg;

    MultipleSubmitLimitRejectedStrategy(Integer code, String msg){
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
        for (MultipleSubmitLimitRejectedStrategy re : MultipleSubmitLimitRejectedStrategy.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static MultipleSubmitLimitRejectedStrategy getRc(Integer code) {
        for (MultipleSubmitLimitRejectedStrategy re : MultipleSubmitLimitRejectedStrategy.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
