package com.damai.redis;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.damai.util.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 缓存对象映射工具
 * @author: 阿星不是程序员
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
     * 检查 redisKeyBuild 中的key是否为空或空的字符串
     *
     * @param redisKeyBuild key包装
     */
    public static void checkNotBlank(RedisKeyBuild redisKeyBuild) {
        if (StringUtil.isEmpty(redisKeyBuild.getRelKey())) {
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
     * @param list key集合
     */
    public static void checkNotEmpty(Collection<?> list) {
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
        if (isEmpty(object)) {
            throw new RuntimeException("请求参数缺失");
        }
    }
    
    /**
     * 判断 object 是否为空
     *
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return StringUtil.isEmpty((String) object);
        }
        if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        }
        return false;
    }

    public static List<String> getBatchKey(Collection<RedisKeyBuild> list){
        return list.stream().map(RedisKeyBuild::getRelKey).collect(Collectors.toList());
    }

    public static <T> List<T> optimizeRedisList(List<T> list){
        if (Objects.isNull(list)) {
            return new ArrayList<>();
        }
        if (list.size() == 0 || Objects.isNull(list.get(0))) {
            return new ArrayList<>();
        }
        return list;
    }
    
    public static boolean checkRedisListIsEmpty(List<?> list){
        if (Objects.isNull(list)) {
            return true;
        }
        return list.size() == 0 || Objects.isNull(list.get(0));
    }
}
