package com.damai.redis;


import com.damai.core.RedisKeyEnum;
import com.damai.core.SpringUtil;

import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: redis key包装
 * @author: 阿宽不是程序员
 **/
public final class RedisKeyWrap {
    /**
     * 实际使用的key
     * */
    private String relKey;

    private RedisKeyWrap() {}

    private RedisKeyWrap(String relKey) {
        this.relKey = relKey;
    }

    /**
     * 构建真实的key
     * @param RedisKeyEnum key的枚举
     * @param args 占位符的值
     * */
    public static RedisKeyWrap createRedisKey(RedisKeyEnum RedisKeyEnum, Object... args){
        String redisRelKey = String.format(RedisKeyEnum.getKeyCode(),args);
        return new RedisKeyWrap(SpringUtil.getPrefixDistinctionName() + "-" + redisRelKey);
    }

    public String getRelKey() {
        return relKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisKeyWrap that = (RedisKeyWrap) o;
        return relKey.equals(that.relKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relKey);
    }
}
