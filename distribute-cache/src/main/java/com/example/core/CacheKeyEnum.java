package com.example.core;


/**
 * @program: distribute-cache
 * @description: redis的key，value管理枚举
 * @author: k
 * @create: 2022-05-28
 **/
public enum CacheKeyEnum {

    Key("key","键值测试","value为TestCacheDto类型","k"),
    Key2("key:%s","键值占位测试","value为TestCacheDto类型","k"),
    
    USER_ID("user_id:%s","userId","value为UserVo类型","k"),
    
    PRODUCT_STOCK("product_stock:%S","商品库存id","value为库存","k"),
    
    //分布式datacenter_id
    DISTRIBUTED_DATACENTER_ID("distributed_datacenter_id:%s","分布式datacenter_id","分布式datacenter_id的值","lk"),
    
    RULE("rule","调用限制规则的key","调用限制规则的value","k"),
    
    RULE_LIMIT("rule_limit","调用限制时间的key","调用限制时间的value","k"),
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

    CacheKeyEnum(String keyCode, String keyMsg, String valueMsg, String author){
        this.keyCode = keyCode;
        this.keyMsg = keyMsg;
        this.valueMsg = valueMsg;
        this.author = author;
    }

    public static CacheKeyEnum getRc(String keyCode) {
        for (CacheKeyEnum re : CacheKeyEnum.values()) {
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
