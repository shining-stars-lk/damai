package com.example.distributecache.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.distributecache.core.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
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
 * @program: distribute-cache
 * @description: redis分布式缓存实现
 * @author: lk
 * @create: 2022-05-28
 **/
@Component
public class RedisCacheImpl implements DistributCache {

    @Qualifier("myStringRedisTemplate")
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public <T> T get(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        // 如果取String类型 则直接取出返回
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (String.class.isAssignableFrom(clazz)) {
            return (T) cachedValue;
        }
        return getComplex(cachedValue, clazz);
    }

    @Override
    public String getRange(CacheKeyWrap cacheKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForValue().get(key, start, end);
    };

    @Override
    public <T> List<T> getValueIsList(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String valueStr = redisTemplate.opsForValue().get(key);
        if (StringUtil.isEmpty(valueStr)) {
            return new ArrayList();
        }
        List<T> resultStrList = JSON.parseArray(valueStr, clazz);
        return resultStrList;
    }


    @Override
    public List<String> getKeys(List<CacheKeyWrap> keyList) {
        CacheUtil.checkNotEmpty(keyList);
        List<String> batchKey = CacheUtil.getBatchKey(keyList);
        return redisTemplate.opsForValue().multiGet(batchKey);
    }

    @Override
    public Boolean hasKey(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long getExpire(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.getExpire(key);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean move(CacheKeyWrap cacheKeyWrap, int dbIndex) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.move(key, dbIndex);
    }

    @Override
    public Boolean persist(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.persist(key);
    }

    @Override
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    @Override
    public void rename(CacheKeyWrap oldKey, CacheKeyWrap newKey) {
        CacheUtil.checkNotBlank(oldKey);
        CacheUtil.checkNotBlank(newKey);
        redisTemplate.rename(oldKey.getRelKey(), newKey.getRelKey());
    }

    @Override
    public Boolean renameIfAbsent(CacheKeyWrap oldKey, CacheKeyWrap newKey) {
        CacheUtil.checkNotBlank(oldKey);
        CacheUtil.checkNotBlank(newKey);
        return redisTemplate.renameIfAbsent(oldKey.getRelKey(), newKey.getRelKey());
    }

    @Override
    public DataType type(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.type(key);
    }
    
    @Override
    public void set(CacheKeyWrap cacheKeyWrap, Object object) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public void set(CacheKeyWrap cacheKeyWrap, Object object, long ttl) {
        set(cacheKeyWrap, object, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void set(CacheKeyWrap cacheKeyWrap, Object object, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        redisTemplate.opsForValue().set(key, json, ttl, timeUnit);
    }

    @Override
    public boolean setIfAbsent(CacheKeyWrap cacheKeyWrap, Object object) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        return redisTemplate.opsForValue().setIfAbsent(key, json);
    }

    @Override
    public Long size(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForValue().size(key);
    }

    @Override
    public void multiSet(Map<CacheKeyWrap, ?> map) {
        CacheUtil.checkNotEmpty(map);
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey.getRelKey(), jsonValue);
        });
        redisTemplate.opsForValue().multiSet(mapForSave);
    }

    @Override
    public boolean multiSetIfAbsent(Map<CacheKeyWrap, ?> map) {
        CacheUtil.checkNotEmpty(map);
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey.getRelKey(), jsonValue);
        });
        return redisTemplate.opsForValue().multiSetIfAbsent(mapForSave);
    }

    @Override
    public Long incrBy(CacheKeyWrap cacheKeyWrap, long increment) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public Double incrByDouble(CacheKeyWrap cacheKeyWrap, double increment) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public Integer append(CacheKeyWrap cacheKeyWrap, String value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForValue().append(key, value);
    }

    @Override
    public void putHash(CacheKeyWrap cacheKeyWrap, String hashKey, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForHash().put(key, hashKey, jsonValue);
    }

    @Override
    public void putHash(CacheKeyWrap cacheKeyWrap, String hashKey, Object value, long ttl) {
        putHash(cacheKeyWrap, hashKey, value, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void putHash(CacheKeyWrap cacheKeyWrap, String hashKey, Object value, long ttl, TimeUnit timeUnit) {
        putHash(cacheKeyWrap, hashKey, value);
        // 设置过期时间
        expire(cacheKeyWrap, ttl, timeUnit);
    }

    @Override
    public void putHash(CacheKeyWrap cacheKeyWrap, Map<String, ?> map) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey, jsonValue);
        });
        redisTemplate.opsForHash().putAll(key, mapForSave);
    }

    @Override
    public void putHash(CacheKeyWrap cacheKeyWrap, Map<String, ?> map, long ttl) {
        putHash(cacheKeyWrap, map, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void putHash(CacheKeyWrap cacheKeyWrap, Map<String, ?> map, long ttl, TimeUnit timeUnit) {
        putHash(cacheKeyWrap, map);
        expire(cacheKeyWrap, ttl, timeUnit);
    }

    @Override
    public Boolean putHashIfAbsent(CacheKeyWrap cacheKeyWrap, String hashKey, Object value){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, jsonValue);
    }

    @Override
    public <T> T getForHash(CacheKeyWrap cacheKeyWrap, String hashKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        // 如果取String类型 则直接取出返回
        if (String.class.isAssignableFrom(clazz)) {
            return (T) o;
        }
        return getComplex(o, clazz);
    }

    @Override
    public <T> List<T> getValueIsListForHash(CacheKeyWrap cacheKeyWrap, String hashKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
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
    public <T> List<T> multiGetForHash(CacheKeyWrap cacheKeyWrap, List<String> hashKeys, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKeys);
        String key = cacheKeyWrap.getRelKey();
        List<Object> objHashKeys = new ArrayList<>();
        objHashKeys.addAll(hashKeys);
        List<Object> multiGetObj = redisTemplate.opsForHash().multiGet(key, objHashKeys);
        if (String.class.isAssignableFrom(clazz)) {
            return (List<T>) multiGetObj;
        }

        return parseObjects(multiGetObj, clazz);
    }

    @Override
    public <T> List<T> getAllForHash(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List<Object> valuesObj = redisTemplate.opsForHash().values(key);
        if (String.class.isAssignableFrom(clazz)) {
            return (List<T>) valuesObj;
        }

        return parseObjects(valuesObj, clazz);
    }

    @Override
    public <T> T indexForList(CacheKeyWrap cacheKeyWrap, long index, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
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
    public Long leftPushForList(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPush(key, jsonValue);
    }

    @Override
    public Long leftPushAllForList(CacheKeyWrap cacheKeyWrap, List<Object> valueList){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(valueList);
        String key = cacheKeyWrap.getRelKey();
        List<String> jsonList = new ArrayList<>(valueList.size());
        valueList.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForList().leftPushAll(key, jsonList);
    }

    @Override
    public Long leftPushIfPresentForList(CacheKeyWrap cacheKeyWrap, Object value){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPushIfPresent(key, jsonValue);
    }

    @Override
    public Long leftPushForList(CacheKeyWrap cacheKeyWrap, Object pivot, Object value){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(pivot);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonPivot = value instanceof String ? (String) pivot : JSON.toJSONString(pivot);
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPush(key, jsonPivot, jsonValue);
    }

    @Override
    public Long rightPushForList(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPush(key, jsonValue);
    }

    @Override
    public Long rightPushAllForList(CacheKeyWrap cacheKeyWrap, List<Object> valueList){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(valueList);
        String key = cacheKeyWrap.getRelKey();
        List<String> jsonList = new ArrayList<>(valueList.size());
        valueList.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForList().rightPushAll(key, jsonList);
    }

    @Override
    public Long rightPushIfPresentForList(CacheKeyWrap cacheKeyWrap, Object value){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPushIfPresent(key, jsonValue);
    }

    @Override
    public Long rightPushForList(CacheKeyWrap cacheKeyWrap, Object pivot, Object value){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(pivot);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonPivot = value instanceof String ? (String) pivot : JSON.toJSONString(pivot);
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPush(key, jsonPivot, jsonValue);
    }

    @Override
    public void setForList(CacheKeyWrap cacheKeyWrap, long index, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForList().set(key, index, jsonValue);
    }

    @Override
    public <T> T leftPopForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().leftPop(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T leftPopBlockForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().leftPop(key, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().rightPop(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopBlockForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String s = redisTemplate.opsForList().rightPop(key, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopAndLeftPushForList(CacheKeyWrap sourceKey, CacheKeyWrap destinationKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(sourceKey);
        CacheUtil.checkNotBlank(destinationKey);
        String sourceRelKey = sourceKey.getRelKey();
        String destinationRelKey = destinationKey.getRelKey();
        String s = redisTemplate.opsForList().rightPopAndLeftPush(sourceRelKey, destinationRelKey);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopBlockAndLeftPushForList(CacheKeyWrap sourceKey, CacheKeyWrap destinationKey, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(sourceKey);
        CacheUtil.checkNotBlank(destinationKey);
        String sourceRelKey = sourceKey.getRelKey();
        String destinationRelKey = destinationKey.getRelKey();
        String s = redisTemplate.opsForList().rightPopAndLeftPush(sourceRelKey, destinationRelKey, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> List<T> getAllForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List list = redisTemplate.opsForList().range(key, 0, -1);
        return parseObjects(list, clazz);
    }

    @Override
    public <T> List<T> rangeForList(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List range = redisTemplate.opsForList().range(key, start, end);
        return parseObjects(range, clazz);
    }

    @Override
    public Long removeForList(CacheKeyWrap cacheKeyWrap, long index, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().remove(key, index, jsonValue);
    }

    @Override
    public void trimForList(CacheKeyWrap cacheKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public Long lenForList(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Boolean hasKeyForHash(CacheKeyWrap cacheKeyWrap, String hashKey) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public void del(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        redisTemplate.delete(key);
    }
    

    @Override
    public Long delForHash(CacheKeyWrap cacheKeyWrap, String hashKey) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Long delForHash(CacheKeyWrap cacheKeyWrap, Collection<String> hashKeys) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKeys);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForHash().delete(key, hashKeys.toArray());
    }

    @Override
    public Long incrByForHash(CacheKeyWrap cacheKeyWrap, String hashKey, long increment) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForHash().increment(key, hashKey, increment);
    }

    @Override
    public Double incrByDoubleForHash(CacheKeyWrap cacheKeyWrap, String hashKey, double delta){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(hashKey);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Set<String> hashKeysForHash(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        return parseObjects(keys,String.class);
    }

    @Override
    public Long sizeForHash(CacheKeyWrap cacheKeyWrap){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public void del(Collection<CacheKeyWrap> keys) {
        CacheUtil.checkNotEmpty(keys);
        List<String> batchKey = CacheUtil.getBatchKey(keys);
        redisTemplate.delete(batchKey);
    }

    @Override
    public Boolean expire(CacheKeyWrap cacheKeyWrap, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.expire(key, ttl, timeUnit);
    }

    @Override
    public Long addForSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().add(key, jsonValue);
    }

    @Override
    public Long addForSet(CacheKeyWrap cacheKeyWrap, List<?> values) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(values);
        String key = cacheKeyWrap.getRelKey();
        List<String> jsonList = new ArrayList<>(values.size());
        values.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForSet().add(key, jsonList.toArray(new String[]{}));
    }

    @Override
    public Long removeForSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().remove(key, jsonValue);
    }

    @Override
    public Long removeForSet(CacheKeyWrap cacheKeyWrap, List<?> values) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(values);
        String key = cacheKeyWrap.getRelKey();
        List<String> jsonList = values.stream()
                .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                .collect(Collectors.toList());
        return redisTemplate.opsForSet().remove(key, jsonList.toArray(new String[]{}));
    }

    @Override
    public <T> T popForSet(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String cachedValue = redisTemplate.opsForSet().pop(key);
        return getComplex(cachedValue,clazz);
    }

    @Override
    public boolean moveForSet(CacheKeyWrap cacheKeyWrap, Object value, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String destKey = destCacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().move(key, jsonValue, destKey);
    }

    @Override
    public Long sizeForSet(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Boolean isMemberForSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().isMember(key, jsonValue);
    }

    @Override
    public <T> Set<T> intersectForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().intersect(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> intersectForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        Set set = redisTemplate.opsForSet().intersect(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long intersectAndStoreForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long intersectAndStoreForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> unionForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().union(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> unionForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        Set set = redisTemplate.opsForSet().union(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long unionAndStoreForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long unionAndStoreForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> differenceForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().difference(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> differenceForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        Set set = redisTemplate.opsForSet().difference(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long differenceForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    @Override
    public Long differenceForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> membersForSet(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set members = redisTemplate.opsForSet().members(key);
        return parseObjects(members,clazz);
    }

    @Override
    public <T> T randomMemberForSet(CacheKeyWrap cacheKeyWrap, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String s = redisTemplate.opsForSet().randomMember(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> List<T> randomMembersForSet(CacheKeyWrap cacheKeyWrap, long count, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List list = redisTemplate.opsForSet().randomMembers(key, count);
        return parseObjects(list,clazz);
    }

    @Override
    public <T> Set<T> distinctRandomMembersForSet(CacheKeyWrap cacheKeyWrap, long count, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForSet().distinctRandomMembers(key, count);
        return parseObjects(set,clazz);
    }

    @Override
    public Cursor<String> scanForSet(CacheKeyWrap cacheKeyWrap, ScanOptions options) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Cursor<String> scan = redisTemplate.opsForSet().scan(key, options);
        return scan;
    }

    @Override
    public void addForZSet(CacheKeyWrap cacheKeyWrap, Object value, Double score){
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        CacheUtil.checkNotEmpty(score);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForZSet().add(key,jsonValue,score);
    }

    @Override
    public void addForZSet(CacheKeyWrap cacheKeyWrap, Object value, Double score, long ttl){
        addForZSet(cacheKeyWrap,value,score,ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void addForZSet(CacheKeyWrap cacheKeyWrap, Object value, Double score, long ttl, TimeUnit timeUnit){
        addForZSet(cacheKeyWrap,value,score);
        expire(cacheKeyWrap, ttl, timeUnit);
    }

    @Override
    public Long addForZSet(CacheKeyWrap cacheKeyWrap, Map<?, Double> map) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
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
    public Long addForZSet(CacheKeyWrap cacheKeyWrap, Map<?, Double> map, long ttl) {
        return addForZSet(cacheKeyWrap, map, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public Long addForZSet(CacheKeyWrap cacheKeyWrap, Map<?, Double> map, long ttl, TimeUnit timeUnit) {
        Long count = addForZSet(cacheKeyWrap, map);
        expire(cacheKeyWrap, ttl, timeUnit);
        return count;
    }

    @Override
    public <T> Set<T> getRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set resultSet = redisTemplate.opsForZSet().range(key, start, end);
        return parseObjects(resultSet,clazz);
    }

    @Override
    public <T> Set<T> getReverseRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set resultSet = redisTemplate.opsForZSet().reverseRange(key, start, end);
        return parseObjects(resultSet,clazz);
    }

    @Override
    public Long delForZSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().remove(key, jsonValue);
    }

    @Override
    public Long delForZSet(CacheKeyWrap cacheKeyWrap, Collection<?> valueCollection) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(valueCollection);
        String key = cacheKeyWrap.getRelKey();
        List<String> jsonValueList = valueCollection.stream()
                .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                .distinct()
                .collect(Collectors.toList());
        return redisTemplate.opsForZSet().remove(key,jsonValueList.toArray());
    }

    @Override
    public Long delRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Double incrementScoreForZSet(CacheKeyWrap cacheKeyWrap, Object value, double delta) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().incrementScore(key, jsonValue, delta);
    }

    @Override
    public Long sizeForZSet(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Long rankForZSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().rank(key, jsonValue);
    }

    @Override
    public Long reverseRankForZSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().reverseRank(key, jsonValue);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        if (cacheSet == null) {
            return new HashSet<>();
        }
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> rangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max,
                                                                              long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeWithScoreForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> reverseRangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeByScoreWithScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> reverseRangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
        return parseObjects(set, clazz);
    }

    @Override
    public Long countForZSet(CacheKeyWrap cacheKeyWrap, double min, double max) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public Long zCardForZSet(CacheKeyWrap cacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Double scoreByValueForZSet(CacheKeyWrap cacheKeyWrap, Object value) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(value);
        String key = cacheKeyWrap.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().score(key, jsonValue);
    }

    @Override
    public Long removeRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Long removeRangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Long unionAndStoreForZSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long unionAndStoreForZSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long intersectAndStoreForZSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotBlank(otherCacheKeyWrap);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        String otherKey = otherCacheKeyWrap.getRelKey();
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long intersectAndStoreForZSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        CacheUtil.checkNotEmpty(otherCacheKeyWraps);
        CacheUtil.checkNotBlank(destCacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherCacheKeyWraps);
        String destKey = destCacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Cursor<ZSetOperations.TypedTuple<String>> scanForZSet(CacheKeyWrap cacheKeyWrap, ScanOptions options) {
        CacheUtil.checkNotBlank(cacheKeyWrap);
        String key = cacheKeyWrap.getRelKey();
        return redisTemplate.opsForZSet().scan(key, options);
    }

    @Override
    public <T> T getByType(CacheKeyWrap cacheKeyWrap, Type genericReturnType){
        String key = cacheKeyWrap.getRelKey();
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
