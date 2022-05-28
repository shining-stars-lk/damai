package com.example.distributecache.repeatLimit.info;

/**
 * @program: distribute-cache
 * @description: 防重复提交触发策略枚举
 * @author: lk
 * @create: 2022-05-28
 **/
public enum RepeatRejectedStrategy {

    ABORT_STRATEGY(1,"ABORT"),
    SAME_RESULT(2,"SAME_RESULT");

    private Integer code;

    private String msg;

    RepeatRejectedStrategy(Integer code, String msg){
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
        for (RepeatRejectedStrategy re : RepeatRejectedStrategy.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static RepeatRejectedStrategy getRc(Integer code) {
        for (RepeatRejectedStrategy re : RepeatRejectedStrategy.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
