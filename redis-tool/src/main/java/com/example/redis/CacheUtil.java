package com.example.redis;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.example.core.StringUtil;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: redis-tool
 * @description: 缓存工具类
 * @author: kuan
 * @create: 2023-05-28
 **/
public class CacheUtil {

    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 构建类型
     *
     * @param types
     * @return
     */
    public static Type buildType(Type... types) {
        ParameterizedTypeImpl beforeType = null;
        if (types != null && types.length > 0) {
            if (types.length == 1) {
                return new ParameterizedTypeImpl(new Type[]{null}, null,
                        types[0]);
            }

            for (int i = types.length - 1; i > 0; i--) {
                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i] : beforeType}, null,
                        types[i - 1]);
            }
        }
        return beforeType;
    }

    /**
     * 检查 Key 是否为空或空的字符串
     *
     * @param key
     */
    public static void checkNotBlank(String... key) {
        for (String s : key) {
            if (StringUtil.isEmpty(s)) {
                throw new RuntimeException("请求参数缺失");
            }
        }
    }

    /**
     * 检查 redisKeyWrap 中的key是否为空或空的字符串
     *
     * @param RedisKeyWrap
     */
    public static void checkNotBlank(RedisKeyWrap RedisKeyWrap) {
        if (StringUtil.isEmpty(RedisKeyWrap.getRelKey())) {
            throw new RuntimeException("请求参数缺失");
        }
    }

    /**
     * 检查 list 是否为空或空的字符串
     *
     * @param list
     */
    public static void checkNotBlank(Collection<String> list) {
        for (String s : list) {
            if (StringUtil.isEmpty(s)) {
                throw new RuntimeException("请求参数缺失");
            }
        }
    }

    /**
     * 检查 list 是否为空或空的字符串
     *
     * @param list
     */
    public static void checkNotEmpty(Collection list) {
        for (Object o : list) {
            if (o == null) {
                throw new RuntimeException("请求参数缺失");
            }
        }
    }

    /**
     * 检查 object 是否为空
     *
     * @param object
     */
    public static void checkNotEmpty(Object object) {
        if (object == null) {
            throw new RuntimeException("请求参数缺失");
        }
    }

    public static List<String> getBatchKey(Collection<RedisKeyWrap> list){
        return list.stream().map(RedisKeyWrap::getRelKey).collect(Collectors.toList());
    }

}
