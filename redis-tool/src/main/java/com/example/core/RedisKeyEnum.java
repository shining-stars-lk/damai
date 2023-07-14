package com.example.core;


/**
 * @program: redis-tool
 * @description: redis的key，value管理枚举
 * @author: kuan
 * @create: 2023-05-28
 **/
public enum RedisKeyEnum {

    Key("key","键值测试","value为TestCacheDto类型","k"),
    Key2("key:%s","键值占位测试","value为TestCacheDto类型","k"),
    
    USER_ID("user_id:%s","userId","value为UserVo类型","k"),
    
    PRODUCT_STOCK("product_stock:%S","商品库存id","value为库存","k"),
    
    //分布式datacenter_id
    DISTRIBUTED_DATACENTER_ID("distributed_datacenter_id:%s","分布式datacenter_id","分布式datacenter_id的值","lk"),
    
    ALL_RULE_HASH("all_rule_hash","所有规则的key","所有规则的Hash","k"),
    RULE("rule","调用限制规则的key","调用限制规则的value","k"),
    
    RULE_LIMIT("rule_limit_%s","调用限制时间的key","调用限制时间的value","k"),
    
    Z_SET_RULE_STAT("z_set_rule_stat_%s","规则zset", "value为zset类型", "k"),
    
    DEPTH_RULE("depth_rule","深度调用限制规则的key","深度调用限制规则的value","k"),
    
    DEPTH_RULE_LIMIT("depth_rule_limit_%s_%s","深度调用限制时间的key","深度调用限制时间的value","k"),
    ;

    /**
     * key值
     * */
    private String keyCode;

    /**
     * key的说明
     * */
    private String keyMsg;

    /**
     * value的说明
     * */
    private String valueMsg;

    /**
     * 作者
     * */
    private String author;

    RedisKeyEnum(String keyCode, String keyMsg, String valueMsg, String author){
        this.keyCode = keyCode;
        this.keyMsg = keyMsg;
        this.valueMsg = valueMsg;
        this.author = author;
    }

    public static RedisKeyEnum getRc(String keyCode) {
        for (RedisKeyEnum re : RedisKeyEnum.values()) {
            if (re.keyCode.equals(keyCode)) {
                return re;
            }
        }
        return null;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyMsg() {
        return keyMsg;
    }

    public void setKeyMsg(String keyMsg) {
        this.keyMsg = keyMsg;
    }

    public String getValueMsg() {
        return valueMsg;
    }

    public void setValueMsg(String valueMsg) {
        this.valueMsg = valueMsg;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
