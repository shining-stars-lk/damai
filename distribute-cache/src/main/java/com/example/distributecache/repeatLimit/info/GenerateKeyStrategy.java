package com.example.distributecache.repeatLimit.info;

/**
 * @program: distribute-cache
 * @description: 生成键策略枚举
 * @author: lk
 * @create: 2022-05-28
 **/
public enum GenerateKeyStrategy {

    SIMPLE_GENERATE_KEY_STRATEGY(1,"SIMPLE_GENERATE_KEY"),
    PARAMETER_GENERATE_KEY_STRATEGY(2,"PARAMETER_GENERATE_KEY");

    private Integer code;

    private String msg;

    GenerateKeyStrategy(Integer code, String msg) {
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
        for (GenerateKeyStrategy re : GenerateKeyStrategy.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static GenerateKeyStrategy getRc(Integer code) {
        for (GenerateKeyStrategy re : GenerateKeyStrategy.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
