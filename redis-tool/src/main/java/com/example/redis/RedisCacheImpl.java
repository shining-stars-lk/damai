package com.example.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.core.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: redis-example
 * @description: redis分布式缓存实现
 * @author: 星哥
 * @create: 2023-05-28
 **/
@AllArgsConstructor
public class RedisCacheImpl implements RedisCache {
    
    private StringRedisTemplate redisTemplate;

    @Override
    public <T> T get(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        // 如果取String类型 则直接取出返回
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (String.class.isAssignableFrom(clazz)) {
            return (T) cachedValue;
        }
        return getComplex(cachedValue, clazz);
    }

    @Override
    public String getRange(RedisKeyWrap RedisKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForValue().get(key, start, end);
    };

    @Override
    public <T> List<T> getValueIsList(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String valueStr = redisTemplate.opsForValue().get(key);
        if (StringUtil.isEmpty(valueStr)) {
            return new ArrayList();
        }
        List<T> resultStrList = JSON.parseArray(valueStr, clazz);
        return resultStrList;
    }


    @Override
    public List<String> getKeys(List<RedisKeyWrap> keyList) {
        CacheUtil.checkNotEmpty(keyList);
        List<String> batchKey = CacheUtil.getBatchKey(keyList);
        return redisTemplate.opsForValue().multiGet(batchKey);
    }

    @Override
    public Boolean hasKey(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long getExpire(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.getExpire(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean move(RedisKeyWrap RedisKeyWrap, int dbIndex) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.move(key, dbIndex);
    }

    @Override
    public Boolean persist(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.persist(key);
    }

    @Override
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    @Override
    public void rename(RedisKeyWrap oldKey, RedisKeyWrap newKey) {
        CacheUtil.checkNotBlank(oldKey);
        CacheUtil.checkNotBlank(newKey);
        redisTemplate.rename(oldKey.getRelKey(), newKey.getRelKey());
    }

    @Override
    public Boolean renameIfAbsent(RedisKeyWrap oldKey, RedisKeyWrap newKey) {
        CacheUtil.checkNotBlank(oldKey);
        CacheUtil.checkNotBlank(newKey);
        return redisTemplate.renameIfAbsent(oldKey.getRelKey(), newKey.getRelKey());
    }

    @Override
    public DataType type(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.type(key);
    }
    
    @Override
    public void set(RedisKeyWrap RedisKeyWrap, Object object) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public void set(RedisKeyWrap RedisKeyWrap, Object object, long ttl) {
        set(RedisKeyWrap, object, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void set(RedisKeyWrap RedisKeyWrap, Object object, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        redisTemplate.opsForValue().set(key, json, ttl, timeUnit);
    }

    @Override
    public boolean setIfAbsent(RedisKeyWrap RedisKeyWrap, Object object) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        return redisTemplate.opsForValue().setIfAbsent(key, json);
    }

    @Override
    public Long size(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForValue().size(key);
    }

    @Override
    public void multiSet(Map<RedisKeyWrap, ?> map) {
        CacheUtil.checkNotEmpty(map);
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey.getRelKey(), jsonValue);
        });
        redisTemplate.opsForValue().multiSet(mapForSave);
    }

    @Override
    public boolean multiSetIfAbsent(Map<RedisKeyWrap, ?> map) {
        CacheUtil.checkNotEmpty(map);
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey.getRelKey(), jsonValue);
        });
        return redisTemplate.opsForValue().multiSetIfAbsent(mapForSave);
    }

    @Override
    public Long incrBy(RedisKeyWrap RedisKeyWrap, long increment) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public Double incrByDouble(RedisKeyWrap RedisKeyWrap, double increment) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public Integer append(RedisKeyWrap RedisKeyWrap, String value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForValue().append(key, value);
    }

    @Override
    public void putHash(RedisKeyWrap RedisKeyWrap, String hashKey, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForHash().put(key, hashKey, jsonValue);
    }

    @Override
    public void putHash(RedisKeyWrap RedisKeyWrap, String hashKey, Object value, long ttl) {
        putHash(RedisKeyWrap, hashKey, value, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void putHash(RedisKeyWrap RedisKeyWrap, String hashKey, Object value, long ttl, TimeUnit timeUnit) {
        putHash(RedisKeyWrap, hashKey, value);
        // 设置过期时间
        expire(RedisKeyWrap, ttl, timeUnit);
    }

    @Override
    public void putHash(RedisKeyWrap RedisKeyWrap, Map<String, ?> map) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey, jsonValue);
        });
        redisTemplate.opsForHash().putAll(key, mapForSave);
    }

    @Override
    public void putHash(RedisKeyWrap RedisKeyWrap, Map<String, ?> map, long ttl) {
        putHash(RedisKeyWrap, map, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void putHash(RedisKeyWrap RedisKeyWrap, Map<String, ?> map, long ttl, TimeUnit timeUnit) {
        putHash(RedisKeyWrap, map);
        expire(RedisKeyWrap, ttl, timeUnit);
    }

    @Override
    public Boolean putHashIfAbsent(RedisKeyWrap RedisKeyWrap, String hashKey, Object value){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, jsonValue);
    }

    @Override
    public <T> T getForHash(RedisKeyWrap RedisKeyWrap, String hashKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        // 如果取String类型 则直接取出返回
        if (String.class.isAssignableFrom(clazz)) {
            return (T) o;
        }
        return getComplex(o, clazz);
    }

    @Override
    public <T> List<T> getValueIsListForHash(RedisKeyWrap RedisKeyWrap, String hashKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        Object o = redisTemplate.opsForHash().get(key,hashKey);
        if (o == null) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        if (o instanceof String) {
            list = JSON.parseArray((String)o, clazz);
        }
        return list;
    }

    @Override
    public <T> List<T> multiGetForHash(RedisKeyWrap RedisKeyWrap, List<String> hashKeys, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKeys);
        String key = RedisKeyWrap.getRelKey();
        List<Object> objHashKeys = new ArrayList<>();
        objHashKeys.addAll(hashKeys);
        List<Object> multiGetObj = redisTemplate.opsForHash().multiGet(key, objHashKeys);
        if (String.class.isAssignableFrom(clazz)) {
            return (List<T>) multiGetObj;
        }

        return parseObjects(multiGetObj, clazz);
    }

    @Override
    public <T> List<T> getAllForHash(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List<Object> valuesObj = redisTemplate.opsForHash().values(key);
        if (String.class.isAssignableFrom(clazz)) {
            return (List<T>) valuesObj;
        }

        return parseObjects(valuesObj, clazz);
    }

    @Override
    public <T> T indexForList(RedisKeyWrap RedisKeyWrap, long index, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String cachedValue = redisTemplate.opsForList().index(key, index);
        if (StringUtil.isEmpty(cachedValue)) {
            return null;
        }
        if (String.class.isAssignableFrom(clazz)) {
            return (T) cachedValue;
        }
        return getComplex(cachedValue, clazz);
    }

    @Override
    public Long leftPushForList(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPush(key, jsonValue);
    }

    @Override
    public Long leftPushAllForList(RedisKeyWrap RedisKeyWrap, List<Object> valueList){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(valueList);
        String key = RedisKeyWrap.getRelKey();
        List<String> jsonList = new ArrayList<>(valueList.size());
        valueList.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForList().leftPushAll(key, jsonList);
    }

    @Override
    public Long leftPushIfPresentForList(RedisKeyWrap RedisKeyWrap, Object value){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPushIfPresent(key, jsonValue);
    }

    @Override
    public Long leftPushForList(RedisKeyWrap RedisKeyWrap, Object pivot, Object value){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(pivot);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonPivot = value instanceof String ? (String) pivot : JSON.toJSONString(pivot);
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPush(key, jsonPivot, jsonValue);
    }

    @Override
    public Long rightPushForList(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPush(key, jsonValue);
    }

    @Override
    public Long rightPushAllForList(RedisKeyWrap RedisKeyWrap, List<Object> valueList){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(valueList);
        String key = RedisKeyWrap.getRelKey();
        List<String> jsonList = new ArrayList<>(valueList.size());
        valueList.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForList().rightPushAll(key, jsonList);
    }

    @Override
    public Long rightPushIfPresentForList(RedisKeyWrap RedisKeyWrap, Object value){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPushIfPresent(key, jsonValue);
    }

    @Override
    public Long rightPushForList(RedisKeyWrap RedisKeyWrap, Object pivot, Object value){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(pivot);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonPivot = value instanceof String ? (String) pivot : JSON.toJSONString(pivot);
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPush(key, jsonPivot, jsonValue);
    }

    @Override
    public void setForList(RedisKeyWrap RedisKeyWrap, long index, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForList().set(key, index, jsonValue);
    }

    @Override
    public <T> T leftPopForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().leftPop(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T leftPopBlockForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().leftPop(key, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().rightPop(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopBlockForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().rightPop(key, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopAndLeftPushForList(RedisKeyWrap sourceKey, RedisKeyWrap destinationKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(sourceKey);
        CacheUtil.checkNotBlank(destinationKey);
        String sourceRelKey = sourceKey.getRelKey();
        String destinationRelKey = destinationKey.getRelKey();
        String s = redisTemplate.opsForList().rightPopAndLeftPush(sourceRelKey, destinationRelKey);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopBlockAndLeftPushForList(RedisKeyWrap sourceKey, RedisKeyWrap destinationKey, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(sourceKey);
        CacheUtil.checkNotBlank(destinationKey);
        String sourceRelKey = sourceKey.getRelKey();
        String destinationRelKey = destinationKey.getRelKey();
        String s = redisTemplate.opsForList().rightPopAndLeftPush(sourceRelKey, destinationRelKey, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> List<T> getAllForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List list = redisTemplate.opsForList().range(key, 0, -1);
        return parseObjects(list, clazz);
    }

    @Override
    public <T> List<T> rangeForList(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List range = redisTemplate.opsForList().range(key, start, end);
        return parseObjects(range, clazz);
    }

    @Override
    public Long removeForList(RedisKeyWrap RedisKeyWrap, long index, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().remove(key, index, jsonValue);
    }

    @Override
    public void trimForList(RedisKeyWrap RedisKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public Long lenForList(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Boolean hasKeyForHash(RedisKeyWrap RedisKeyWrap, String hashKey) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public void del(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        redisTemplate.delete(key);
    }
    

    @Override
    public Long delForHash(RedisKeyWrap RedisKeyWrap, String hashKey) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Long delForHash(RedisKeyWrap RedisKeyWrap, Collection<String> hashKeys) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKeys);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForHash().delete(key, hashKeys.toArray());
    }

    @Override
    public Long incrByForHash(RedisKeyWrap RedisKeyWrap, String hashKey, long increment) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForHash().increment(key, hashKey, increment);
    }

    @Override
    public Double incrByDoubleForHash(RedisKeyWrap RedisKeyWrap, String hashKey, double delta){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Set<String> hashKeysForHash(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        return parseObjects(keys,String.class);
    }

    @Override
    public Long sizeForHash(RedisKeyWrap RedisKeyWrap){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public void del(Collection<RedisKeyWrap> keys) {
        CacheUtil.checkNotEmpty(keys);
        List<String> batchKey = CacheUtil.getBatchKey(keys);
        redisTemplate.delete(batchKey);
    }

    @Override
    public Boolean expire(RedisKeyWrap RedisKeyWrap, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.expire(key, ttl, timeUnit);
    }

    @Override
    public Long addForSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().add(key, jsonValue);
    }

    @Override
    public Long addForSet(RedisKeyWrap RedisKeyWrap, List<?> values) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(values);
        String key = RedisKeyWrap.getRelKey();
        List<String> jsonList = new ArrayList<>(values.size());
        values.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForSet().add(key, jsonList.toArray(new String[]{}));
    }

    @Override
    public Long removeForSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().remove(key, jsonValue);
    }

    @Override
    public Long removeForSet(RedisKeyWrap RedisKeyWrap, List<?> values) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(values);
        String key = RedisKeyWrap.getRelKey();
        List<String> jsonList = values.stream()
                .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                .collect(Collectors.toList());
        return redisTemplate.opsForSet().remove(key, jsonList.toArray(new String[]{}));
    }

    @Override
    public <T> T popForSet(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String cachedValue = redisTemplate.opsForSet().pop(key);
        return getComplex(cachedValue,clazz);
    }

    @Override
    public boolean moveForSet(RedisKeyWrap RedisKeyWrap, Object value, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String destKey = destRedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().move(key, jsonValue, destKey);
    }

    @Override
    public Long sizeForSet(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Boolean isMemberForSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().isMember(key, jsonValue);
    }

    @Override
    public <T> Set<T> intersectForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().intersect(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> intersectForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        Set set = redisTemplate.opsForSet().intersect(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long intersectAndStoreForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long intersectAndStoreForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> unionForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().union(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> unionForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        Set set = redisTemplate.opsForSet().union(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long unionAndStoreForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long unionAndStoreForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> differenceForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().difference(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> differenceForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        Set set = redisTemplate.opsForSet().difference(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long differenceForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    @Override
    public Long differenceForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> membersForSet(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set members = redisTemplate.opsForSet().members(key);
        return parseObjects(members,clazz);
    }

    @Override
    public <T> T randomMemberForSet(RedisKeyWrap RedisKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String s = redisTemplate.opsForSet().randomMember(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> List<T> randomMembersForSet(RedisKeyWrap RedisKeyWrap, long count, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List list = redisTemplate.opsForSet().randomMembers(key, count);
        return parseObjects(list,clazz);
    }

    @Override
    public <T> Set<T> distinctRandomMembersForSet(RedisKeyWrap RedisKeyWrap, long count, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().distinctRandomMembers(key, count);
        return parseObjects(set,clazz);
    }

    @Override
    public Cursor<String> scanForSet(RedisKeyWrap RedisKeyWrap, ScanOptions options) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Cursor<String> scan = redisTemplate.opsForSet().scan(key, options);
        return scan;
    }

    @Override
    public void addForZSet(RedisKeyWrap RedisKeyWrap, Object value, Double score){
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        CacheUtil.checkNotEmpty(score);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForZSet().add(key,jsonValue,score);
    }

    @Override
    public void addForZSet(RedisKeyWrap RedisKeyWrap, Object value, Double score, long ttl){
        addForZSet(RedisKeyWrap,value,score,ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void addForZSet(RedisKeyWrap RedisKeyWrap, Object value, Double score, long ttl, TimeUnit timeUnit){
        addForZSet(RedisKeyWrap,value,score);
        expire(RedisKeyWrap, ttl, timeUnit);
    }

    @Override
    public Long addForZSet(RedisKeyWrap RedisKeyWrap, Map<?, Double> map) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> collect =
                map.entrySet()
                        .stream()
                        .map(item -> {
                            String entryKey = item.getKey() instanceof String ? (String) item.getKey() : JSON.toJSONString(item.getKey());
                            return new DefaultTypedTuple<>(entryKey, item.getValue());
                        })
                        .collect(Collectors.toSet());
        return redisTemplate.opsForZSet().add(key, collect);
    }

    @Override
    public Long addForZSet(RedisKeyWrap RedisKeyWrap, Map<?, Double> map, long ttl) {
        return addForZSet(RedisKeyWrap, map, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public Long addForZSet(RedisKeyWrap RedisKeyWrap, Map<?, Double> map, long ttl, TimeUnit timeUnit) {
        Long count = addForZSet(RedisKeyWrap, map);
        expire(RedisKeyWrap, ttl, timeUnit);
        return count;
    }

    @Override
    public <T> Set<T> getRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set resultSet = redisTemplate.opsForZSet().range(key, start, end);
        return parseObjects(resultSet,clazz);
    }

    @Override
    public <T> Set<T> getReverseRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set resultSet = redisTemplate.opsForZSet().reverseRange(key, start, end);
        return parseObjects(resultSet,clazz);
    }

    @Override
    public Long delForZSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().remove(key, jsonValue);
    }

    @Override
    public Long delForZSet(RedisKeyWrap RedisKeyWrap, Collection<?> valueCollection) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(valueCollection);
        String key = RedisKeyWrap.getRelKey();
        List<String> jsonValueList = valueCollection.stream()
                .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                .distinct()
                .collect(Collectors.toList());
        return redisTemplate.opsForZSet().remove(key,jsonValueList.toArray());
    }

    @Override
    public Long delRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Double incrementScoreForZSet(RedisKeyWrap RedisKeyWrap, Object value, double delta) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().incrementScore(key, jsonValue, delta);
    }

    @Override
    public Long sizeForZSet(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Long rankForZSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().rank(key, jsonValue);
    }

    @Override
    public Long reverseRankForZSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().reverseRank(key, jsonValue);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        if (cacheSet == null) {
            return new HashSet<>();
        }
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> rangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max,
                                                                              long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeWithScoreForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> reverseRangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeByScoreWithScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> reverseRangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
        return parseObjects(set, clazz);
    }

    @Override
    public Long countForZSet(RedisKeyWrap RedisKeyWrap, double min, double max) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public Long zCardForZSet(RedisKeyWrap RedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Double scoreByValueForZSet(RedisKeyWrap RedisKeyWrap, Object value) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = RedisKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().score(key, jsonValue);
    }

    @Override
    public Long removeRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Long removeRangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Long unionAndStoreForZSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long unionAndStoreForZSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long intersectAndStoreForZSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotBlank(otherRedisKeyWrap);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        String otherKey = otherRedisKeyWrap.getRelKey();
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long intersectAndStoreForZSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        CacheUtil.checkNotEmpty(otherRedisKeyWraps);
        CacheUtil.checkNotBlank(destRedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyWraps);
        String destKey = destRedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Cursor<ZSetOperations.TypedTuple<String>> scanForZSet(RedisKeyWrap RedisKeyWrap, ScanOptions options) {
        CacheUtil.checkNotBlank(RedisKeyWrap);
        String key = RedisKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().scan(key, options);
    }

    @Override
    public <T> T getByType(RedisKeyWrap RedisKeyWrap, Type genericReturnType){
        String key = RedisKeyWrap.getRelKey();
        String s = redisTemplate.boundValueOps(key).get();
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        return JSONObject.parseObject(s ,genericReturnType);
    }

    @Override
    public RedisTemplate getInstance() {
        return redisTemplate;
    }


    public <T> T getComplex(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        if (clazz.isAssignableFrom(String.class)) {
            if (source instanceof String) {
                return (T)source;
            }else{
                return (T)JSON.toJSONString(source);
            }
        }
        return source instanceof String ? JSON.parseObject((String) source, CacheUtil.buildType(clazz)) : null;
    }

    public <T> List<T> parseObjects(List<Object> sources, Class<T> clazz) {
        if (sources == null) {
            return new ArrayList<>();
        }
        if (clazz.isAssignableFrom(String.class)) {
            List<T> resultList = (List<T>) sources.stream()
                    .map(each -> each instanceof String ? (String) each : JSON.toJSONString(each))
                    .collect(Collectors.toList());
            return resultList;
        }
        List<T> resultList = (List<T>) sources.stream()
                .map(each -> each instanceof String ? JSON.parseObject((String) each, CacheUtil.buildType(clazz)) : null)
                .collect(Collectors.toList());
        return resultList;
    }

    public <T> Set<T> parseObjects(Set<Object> sources, Class<T> clazz) {
        if (sources == null) {
            return new HashSet<>();
        }
        if (clazz.isAssignableFrom(String.class)) {
            Set<T> resultSet = (Set<T>) sources.stream()
                    .map(each -> each instanceof String ? (String) each : JSON.toJSONString(each))
                    .collect(Collectors.toSet());
            return resultSet;
        }
        Set<T> resultSet = (Set<T>) sources.stream()
                .map(each -> each instanceof String ? JSON.parseObject((String) each, CacheUtil.buildType(clazz)) : null)
                .collect(Collectors.toSet());
        return resultSet;
    }

    public <T> Set<ZSetOperations.TypedTuple<T>> typedTupleStringParseObjects(Set<ZSetOperations.TypedTuple<String>> sources, Class<T> clazz){
        if (sources == null) {
            return new HashSet<>();
        }
        Set<ZSetOperations.TypedTuple<T>> set = new HashSet<>(sources.size());
        for (ZSetOperations.TypedTuple<String> typedTuple : sources) {
            String value = typedTuple.getValue();
            T complex = getComplex(value, clazz);
            Double score = typedTuple.getScore();
            DefaultTypedTuple defaultTypedTuple = new DefaultTypedTuple(complex,score);
            set.add(defaultTypedTuple);
        }
        return set;
    }

    public <T> Set<ZSetOperations.TypedTuple<T>> typedTupleParseObjects(Set<ZSetOperations.TypedTuple> sources, Class<T> clazz){
        if (sources == null) {
            return new HashSet<>();
        }
        Set<ZSetOperations.TypedTuple<T>> set = new HashSet<>(sources.size());
        for (ZSetOperations.TypedTuple typedTuple : sources) {
            Object value = typedTuple.getValue();
            T complex = getComplex(value, clazz);
            Double score = typedTuple.getScore();
            DefaultTypedTuple defaultTypedTuple = new DefaultTypedTuple(complex,score);
            set.add(defaultTypedTuple);
        }
        return set;
    }
}
