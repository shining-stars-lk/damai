package com.example.distributecache.redis;


import com.example.distributecache.core.CacheKeyEnum;
import java.util.Objects;

/**
 * @program: distribute-cache
 * @description: 真实key构建类(不对外使用,只在缓存工具类中做中转)
 * @author: lk
 * @create: 2022-05-28
 **/
public final class CacheKeyWrap {
    /**
     * 实际使用的key
     * */
    private String relKey;

    private CacheKeyWrap() {}

    private CacheKeyWrap(String relKey) {
        this.relKey = relKey;
    }

    /**
     * 构建真实的key
     * @param cacheKeyEnum key的枚举
     * @param args 占位符的值
     * */
    public static CacheKeyWrap cacheKeyBuild(CacheKeyEnum cacheKeyEnum, Object... args){
        String redisRelKey = String.format(cacheKeyEnum.getKeyCode(),args);
        return new CacheKeyWrap(redisRelKey);
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
        CacheKeyWrap that = (CacheKeyWrap) o;
        return relKey.equals(that.relKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relKey);
    }
}
