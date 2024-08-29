package com.damai.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis方法抽象
 * @author: 阿星不是程序员
 **/
public interface RedisCache {

    /**
     * 获取字符串对象
     *
     * @param redisKeyBuild   RedisKeyBuild
     * @param clazz 类对象
     * @param <T>   T
     * @return T 普通对象
     */
    <T> T get(RedisKeyBuild redisKeyBuild, Class<T> clazz);
    
    /**
     * 获取字符串对象(如果缓存中不存在，则执行给定的supplier接口)
     *
     * @param redisKeyBuild   RedisKeyBuild
     * @param clazz 类对象
     * @param <T>   T
     * @param supplier 缓存为空时，执行的逻辑
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return T 普通对象
     */
    <T> T get(RedisKeyBuild redisKeyBuild, Class<T> clazz, Supplier<T> supplier, long ttl, TimeUnit timeUnit);

    /**
     * 返回 key 中字符串值的子字符
     * @param redisKeyBuild 缓存key
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    String getRange(RedisKeyBuild redisKeyBuild, long start, long end);

    /**
     * 获取字符串对象, 并且字符串中是集合内容
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @param <T> 指定泛型
     * @return List<T>
     */
    <T> List<T> getValueIsList(RedisKeyBuild redisKeyBuild, Class<T> clazz);
    
    /**
     * 获取字符串对象, 并且字符串中是集合内容(如果缓存中不存在，则执行给定的supplier接口)
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @param <T> 指定泛型
     * @param supplier 缓存为空时，执行的逻辑
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return List<T>
     */
    <T> List<T> getValueIsList(RedisKeyBuild redisKeyBuild, Class<T> clazz, Supplier<List<T>> supplier, long ttl, TimeUnit timeUnit);


    /**
     * 通过多个key批量获取多个value
     *
     * @param keyList key集合
     * @return List<String>
     */
    List<String> getKeys(List<RedisKeyBuild> keyList);

    /**
     * 判断key是否存在
     *
     * @param redisKeyBuild redisKeyBuild
     * @return 是否存在 可能为空
     */
    Boolean hasKey(RedisKeyBuild redisKeyBuild);

    /**
     * 删除key
     *
     * @param redisKeyBuild 缓存key
     * @return
     */
    void del(RedisKeyBuild redisKeyBuild);


    /**
     * 批量删除key
     *
     * @param keys key集合
     */
    void del(Collection<RedisKeyBuild> keys);

    /**
     * 设置key过期时间
     *
     * @param redisKeyBuild      RedisKeyBuild
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return 是否成功
     */
    Boolean expire(RedisKeyBuild redisKeyBuild, long ttl, TimeUnit timeUnit);

    /**
     * 获取key超时时间
     *
     * @param redisKeyBuild redisKeyBuild
     * @return 超时时间
     */
    Long getExpire(RedisKeyBuild redisKeyBuild);
    
    /**
     * 获取key超时时间
     *
     * @param redisKeyBuild redisKeyBuild
     * @param timeUnit 时间单位
     * @return 超时时间
     */
    Long getExpire(RedisKeyBuild redisKeyBuild,TimeUnit timeUnit);

    /**
     * 查找匹配的key
     *
     * @param pattern 缓存key
     * @return keys
     */
    Set<String> keys(String pattern);

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param redisKeyBuild 缓存key
     * @param dbIndex
     * @return
     */
    Boolean move(RedisKeyBuild redisKeyBuild, int dbIndex);

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param redisKeyBuild 缓存key
     * @return
     */
    Boolean persist(RedisKeyBuild redisKeyBuild);

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return
     */
    String randomKey();

    /**
     * 修改 key 的名称
     *
     * @param oldKey 缓存key
     * @param newKey 缓存key
     */
    void rename(RedisKeyBuild oldKey, RedisKeyBuild newKey);

    /**
     * 仅当 newKey 不存在时，将 oldKey 改名为 newKey
     *
     * @param oldKey 缓存key
     * @param newKey 缓存key
     * @return
     */
    Boolean renameIfAbsent(RedisKeyBuild oldKey, RedisKeyBuild newKey);

    /**
     * 返回 key 所储存的值的类型
     *
     * @param redisKeyBuild 缓存key
     * @return
     */
    DataType type(RedisKeyBuild redisKeyBuild);

    /**
     * 设置缓存
     *
     * @param redisKeyBuild        缓存key
     * @param object     缓存对象
     */
    void set(RedisKeyBuild redisKeyBuild, Object object);

    /**
     * 设置缓存
     *
     * @param redisKeyBuild        缓存key
     * @param object     缓存对象
     * @param ttl 过期时间
     */
    void set(RedisKeyBuild redisKeyBuild, Object object, long ttl);

    /**
     * 设置缓存
     *
     * @param redisKeyBuild      缓存key
     * @param object   缓存对象
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    void set(RedisKeyBuild redisKeyBuild, Object object, long ttl, TimeUnit timeUnit);

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param redisKeyBuild  缓存key
     * @param object 对象
     * @return 之前已经存在返回false,不存在返回true
     */
    boolean setIfAbsent(RedisKeyBuild redisKeyBuild, Object object);

    /**
     * 获取字符串的长度
     *
     * @param redisKeyBuild 缓存key
     * @return 长度
     */
    Long size(RedisKeyBuild redisKeyBuild);

    /**
     * 批量添加
     *
     * @param map 对象
     */
    void multiSet(Map<RedisKeyBuild, ?> map);

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param map 对象
     * @return 之前已经存在返回false,不存在返回true
     */
    boolean multiSetIfAbsent(Map<RedisKeyBuild, ?> map);

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param redisKeyBuild 缓存key
     * @param increment 步长
     * @return
     */
    Long incrBy(RedisKeyBuild redisKeyBuild, long increment);

    /**
     * double类型增加(自增长), 负数则为自减
     * @param redisKeyBuild 缓存key
     * @param increment 步长
     * @return
     */
    Double incrByDouble(RedisKeyBuild redisKeyBuild, double increment);

    /**
     * 追加到末尾
     *
     * @param redisKeyBuild 缓存key
     * @param value 值
     * @return
     */
    Integer append(RedisKeyBuild redisKeyBuild, String value);

    /** -------------------hash相关操作------------------------- */

    /**
     * 放置一个键值对
     *
     * @param redisKeyBuild     hash键
     * @param hashKey hash key
     * @param value   hash value
     */
    void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value);

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param redisKeyBuild           hash键
     * @param hashKey       hash key
     * @param value         hash value
     * @param ttl    过期时间
     */
    void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value, long ttl);

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param redisKeyBuild       hash键
     * @param hashKey   hash key
     * @param value     hash value
     * @param ttl       过期时间
     * @param timeUnit  时间单位
     */
    void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value, long ttl, TimeUnit timeUnit);

    /**
     * 放入map中所有键值对
     *
     * @param redisKeyBuild key
     * @param map hash
     */
    void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map);

    /**
     * 放入map中所有键值对 并设置过期时间
     *
     * @param redisKeyBuild key
     * @param map hash
     * @param ttl 过期时间
     */
    void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map, long ttl);

    /**
     * 放入 Map 中所有键值对 并设置过期时间和时间单位
     *
     * @param redisKeyBuild key
     * @param map hash
     * @param ttl 过期时间
     * @param timeUnit 时间单位
     */
    void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map, long ttl, TimeUnit timeUnit);

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param redisKeyBuild 缓存key
     * @param hashKey hash中key
     * @param value 对象
     * @return
     */
    Boolean putHashIfAbsent(RedisKeyBuild redisKeyBuild, String hashKey, Object value);

    /**
     * 从 Hash 中获取普通对象
     *
     * @param redisKeyBuild     key
     * @param hashKey hash key
     * @param clazz   类对象
     * @param <T>     T
     * @return 普通对象
     */
    @SuppressWarnings("all")
    <T> T getForHash(RedisKeyBuild redisKeyBuild, String hashKey, Class<T> clazz);

    /**
     * Hash的value是字符串集合，进行提取
     *
     * @param redisKeyBuild     key
     * @param hashKey hash key
     * @param clazz   类对象
     * @param <T>     T
     * @return 普通对象
     */
    <T> List<T> getValueIsListForHash(RedisKeyBuild redisKeyBuild, String hashKey, Class<T> clazz);

    /**
     * 从 {@code key} 处获取给定 {@code hashKeys} 的值
     *
     * @param redisKeyBuild      key
     * @param hashKeys hashKeys
     * @param clazz    类对象
     * @param <T>      T
     * @return
     */
    <T> List<T> multiGetForHash(RedisKeyBuild redisKeyBuild, List<String> hashKeys, Class<T> clazz);

    /**
     * 谨慎使用！
     * 获取 Hash Key 下所有值
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @param <T> 泛型
     * @return
     */
    <T> List<T> getAllForHash(RedisKeyBuild redisKeyBuild, Class<T> clazz);
    
    /**
     * 谨慎使用！
     * 获取 Hash Key 下所有值，返回值为map
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @param <T> 泛型
     * @return
     */
    <T> Map<String,T> getAllMapForHash(RedisKeyBuild redisKeyBuild, Class<T> clazz);
    /**
     * 判断hash中 key是否存在
     *
     * @param redisKeyBuild 缓存key
     * @param hashKey hash中key
     * @return 结果
     */
    Boolean hasKeyForHash(RedisKeyBuild redisKeyBuild, String hashKey);

    /**
     * 删除hash key
     *
     * @param redisKeyBuild 缓存key
     * @param hashKey hash中key
     * @return 结果
     */
    Long delForHash(RedisKeyBuild redisKeyBuild, String hashKey);

    /**
     * 批量删除hash key
     *
     * @param redisKeyBuild 缓存key
     * @param hashKeys hash中key
     * @return 结果
     */
    Long delForHash(RedisKeyBuild redisKeyBuild, Collection<String> hashKeys);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param redisKeyBuild 缓存key
     * @param hashKey hash中key
     * @param increment 步长
     * @return 结果
     */
    Long incrByForHash(RedisKeyBuild redisKeyBuild, String hashKey, long increment);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment(double类型)
     *
     * @param redisKeyBuild 缓存key
     * @param hashKey hash中key
     * @param delta 步长
     * @return 结果
     */
    Double incrByDoubleForHash(RedisKeyBuild redisKeyBuild, String hashKey, double delta);

    /**
     * 获取所有哈希表中的hashKey
     *
     * @param redisKeyBuild 缓存key
     * @return 结果
     */
    Set<String> hashKeysForHash(RedisKeyBuild redisKeyBuild);

    /**
     * 获取哈希表中字段的数量
     *
     * @param redisKeyBuild 缓存key
     * @return 结果
     */
    Long sizeForHash(RedisKeyBuild redisKeyBuild);

    /** ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param redisKeyBuild 缓存key
     * @param index 索引
     * @param clazz 类型
     * @return 结果
     */
    <T> T indexForList(RedisKeyBuild redisKeyBuild, long index, Class<T> clazz);

    /**
     * List 从左边放入元素
     *
     * @param redisKeyBuild   key
     * @param value value
     * @return 改动行数
     */
    Long leftPushForList(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * List 从左边放入元素
     *
     * @param redisKeyBuild   key
     * @param valueList valueList
     * @return 改动行数
     */
    Long leftPushAllForList(RedisKeyBuild redisKeyBuild, List<?> valueList);

    /**
     * List 从左边放入元素(当list存在的时候才加入)
     *
     * @param redisKeyBuild
     * @param value
     * @return
     */
    Long leftPushIfPresentForList(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * 如果pivot存在,在pivot左边添加
     *
     * @param redisKeyBuild 缓存key
     * @param pivot pivot
     * @param value 对象
     * @return 结果
     */
    Long leftPushForList(RedisKeyBuild redisKeyBuild, Object pivot, Object value);

    /**
     * List 从右边放入元素
     *
     * @param redisKeyBuild   key
     * @param value value
     * @return 改动行数
     */
    Long rightPushForList(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * List 从右边放入元素
     *
     * @param redisKeyBuild   key
     * @param valueList valueList
     * @return 改动行数
     */
    Long rightPushAllForList(RedisKeyBuild redisKeyBuild, List<Object> valueList);

    /**
     * List 从右边放入元素(当list存在的时候才加入)
     *
     * @param redisKeyBuild 缓存key
     * @param value 对象
     * @return 结果
     */
    Long rightPushIfPresentForList(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * 如果pivot存在,在pivot右边添加
     *
     * @param redisKeyBuild 缓存key
     * @param pivot pivot
     * @param value 对象
     * @return 结果
     */
    Long rightPushForList(RedisKeyBuild redisKeyBuild, Object pivot, Object value);

    /**
     * 通过索引设置列表元素的值
     *
     * @param redisKeyBuild 缓存key
     * @param index
     *            位置
     * @param value 对象
     */
    void setForList(RedisKeyBuild redisKeyBuild, long index, Object value);

    /**
     * 移出并获取列表的第一个元素
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @return 删除的元素
     */
    <T> T leftPopForList(RedisKeyBuild redisKeyBuild, Class<T> clazz);

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return
     */
    <T> T leftPopBlockForList(RedisKeyBuild redisKeyBuild, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 移除并获取列表最后一个元素
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @return 删除的元素
     */
    <T> T rightPopForList(RedisKeyBuild redisKeyBuild, Class<T> clazz);

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param redisKeyBuild 缓存key
     * @param clazz 类型
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return
     */
    <T> T rightPopBlockForList(RedisKeyBuild redisKeyBuild, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey
     * @param destinationKey
     * @param clazz
     * @return
     */
    <T> T rightPopAndLeftPushForList(RedisKeyBuild sourceKey, RedisKeyBuild destinationKey, Class<T> clazz);

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey 缓存key
     * @param destinationKey 弹出key
     * @param clazz 类型
     * @param timeout 时间
     * @param unit 时间单位
     * @return 结果
     */
    <T> T rightPopBlockAndLeftPushForList(RedisKeyBuild sourceKey, RedisKeyBuild destinationKey, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 获取 List 全部数据
     *
     * @param redisKeyBuild 缓存key
     * @param <T> 泛型
     * @param clazz 类型
     * @return
     */
    <T> List<T> getAllForList(RedisKeyBuild redisKeyBuild, Class<T> clazz);

    /**
     * 获取列表指定范围内的元素
     *
     * @param redisKeyBuild 缓存key
     * @param start 开始位置, 0是开始位置
     * @param end 结束位置, -1返回所有
     * @param clazz 类型
     * @return 结果
     */
    <T> List<T> rangeForList(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz);


    /**
     * 删除集合中值等于value得元素
     *
     * @param redisKeyBuild 缓存key
     * @param index
     *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *            index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value 对象
     * @return 结果
     */
    Long removeForList(RedisKeyBuild redisKeyBuild, long index, Object value);


    /**
     * 裁剪list
     *
     * @param redisKeyBuild
     * @param start
     * @param end
     */
    void trimForList(RedisKeyBuild redisKeyBuild, long start, long end);

    /**
     * 获取列表长度
     *
     * @param redisKeyBuild
     * @return
     */
    Long lenForList(RedisKeyBuild redisKeyBuild);


    /** --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param redisKeyBuild
     * @param value
     * @return
     */
    Long addForSet(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * set批量添加元素
     *
     * @param redisKeyBuild
     * @param values
     * @return
     */
    Long addForSet(RedisKeyBuild redisKeyBuild, List<?> values);

    /**
     * set移除元素
     *
     * @param redisKeyBuild
     * @param value
     * @return
     */
    Long removeForSet(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * set批量移除元素
     *
     * @param redisKeyBuild
     * @param values
     * @return
     */
    Long removeForSet(RedisKeyBuild redisKeyBuild, List<?> values);

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param redisKeyBuild
     * @param clazz
     * @return
     */
    <T> T popForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz);

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param redisKeyBuild
     * @param value
     * @param destRedisKeyBuild
     * @return
     */
    boolean moveForSet(RedisKeyBuild redisKeyBuild, Object value, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取集合的大小
     *
     * @param redisKeyBuild
     * @return
     */
    Long sizeForSet(RedisKeyBuild redisKeyBuild);

    /**
     * 判断集合是否包含value
     *
     * @param redisKeyBuild
     * @param value
     * @return
     */
    Boolean isMemberForSet(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * 获取两个集合的交集
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param clazz
     * @return
     */
    <T> Set<T> intersectForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, Class<T> clazz);

    /**
     * 获取key集合与多个集合的交集
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param clazz
     * @return
     */
    <T> Set<T> intersectForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, Class<T> clazz);

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param destRedisKeyBuild
     * @return
     */
    Long intersectAndStoreForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild);

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param destRedisKeyBuild
     * @return
     */
    Long intersectAndStoreForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取两个集合的并集
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param clazz
     * @return
     */
    <T> Set<T> unionForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, Class<T> clazz);

    /**
     * 获取key集合与多个集合的并集
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param clazz
     * @return
     */
    <T> Set<T> unionForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, Class<T> clazz);

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param destRedisKeyBuild
     * @return
     */
    Long unionAndStoreForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild);

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param destRedisKeyBuild
     * @return
     */
    Long unionAndStoreForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取两个集合的差集
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param clazz
     * @return
     */
    <T> Set<T> differenceForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, Class<T> clazz);

    /**
     * 获取key集合与多个集合的差集
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param clazz
     * @return
     */
    <T> Set<T> differenceForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, Class<T> clazz);

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param destRedisKeyBuild
     * @return
     */
    Long differenceForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild);

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param destRedisKeyBuild
     * @return
     */
    Long differenceForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取集合所有元素
     *
     * @param redisKeyBuild
     * @param clazz
     * @return
     */
    <T> Set<T> membersForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz);

    /**
     * 随机获取集合中的一个元素
     *
     * @param redisKeyBuild
     * @param clazz
     * @return
     */
    <T> T randomMemberForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz);

    /**
     * 随机获取集合中count个元素
     *
     * @param redisKeyBuild
     * @param count
     * @param clazz
     * @return
     */
    <T> List<T> randomMembersForSet(RedisKeyBuild redisKeyBuild, long count, Class<T> clazz);

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param redisKeyBuild
     * @param count
     * @param clazz
     * @return
     */
    <T> Set<T> distinctRandomMembersForSet(RedisKeyBuild redisKeyBuild, long count, Class<T> clazz);

    /**
     * 游标遍历
     * @param redisKeyBuild
     * @param options
     * @return
     */
    Cursor<String> scanForSet(RedisKeyBuild redisKeyBuild, ScanOptions options);



    /**------------------SortedSet相关操作--------------------------------*/

    /**
     * 存储有序列表
     *
     * @param redisKeyBuild key
     * @param value value
     * @param score 评分值
     * @return
     */
    void addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, Double score);

    /**
     * 存储有序列表并设置超时时间(秒)
     *
     * @param redisKeyBuild key
     * @param value value
     * @param score 评分值
     * @param ttl 超时时间
     * @return
     */
    void addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, Double score, long ttl);

    /**
     * 存储有序列表并设置超时时间
     *
     * @param redisKeyBuild key
     * @param value value
     * @param score 评分值
     * @param ttl 超时时间
     * @param timeUnit 时间单位
     * @return
     */
    void addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, Double score, long ttl, TimeUnit timeUnit);

    /**
     * 存储有序列表
     *
     * @param redisKeyBuild
     * @param map map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @return
     */
    Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map);

    /**
     * 存储有序列表并设置超时时间(秒)
     *
     * @param redisKeyBuild
     * @param map map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @param ttl
     * @return
     */
    Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map, long ttl);

    /**
     * 存储有序列表并设置超时时间 自定义单位
     *
     * @param redisKeyBuild      key
     * @param map      map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @param ttl      过期时间
     * @param timeUnit 过期时间单位
     * @return 影响的条目
     */
    Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map, long ttl, TimeUnit timeUnit);

    /**
     * 获取有序列表中范围条目,并转为指定类型
     *
     * @param redisKeyBuild   key
     * @param start 开始下标 从0开始
     * @param end   结束下标 包含此条
     * @param clazz 序列化类型
     * @param <T>   泛型参数
     * @return 结果set集合
     */
    <T> Set<T> getRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz);

    /**
     * 反转获取有序列表中范围条目,并转为指定类型
     *
     * @param redisKeyBuild   key
     * @param start 开始下标 从0开始
     * @param end   结束下标 包含此条
     * @param clazz 序列化类型
     * @param <T>   泛型参数
     * @return 结果set集合
     */
    <T> Set<T> getReverseRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz);

    /**
     * 删除zSet条目
     *
     * @param redisKeyBuild   key
     * @param value 数据
     * @return 影响条目
     */
    Long delForSortedSet(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * 批量删除zSet条目
     *
     * @param redisKeyBuild             key
     * @param valueCollection 数据
     * @return 影响条目
     */
    Long delForSortedSet(RedisKeyBuild redisKeyBuild, Collection<?> valueCollection);

    /**
     * 删除范围元素
     *
     * @param redisKeyBuild   key
     * @param start 开始range
     * @param end   结束range
     * @return 影响条目
     */
    Long delRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end);


    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param redisKeyBuild key
     * @param value 对象
     * @param delta 值
     * @return 结果
     */
    Double incrementScoreForSortedSet(RedisKeyBuild redisKeyBuild, Object value, double delta);



    /**
     * 计算zSet总条数
     *
     * @param redisKeyBuild key
     * @return 总条数  不存在则空
     */
    Long sizeForSortedSet(RedisKeyBuild redisKeyBuild);

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param redisKeyBuild   key
     * @param value value
     * @return rank
     */
    Long rankForSortedSet(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param redisKeyBuild
     * @param value
     * @return
     */
    Long reverseRankForSortedSet(RedisKeyBuild redisKeyBuild, Object value);


    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param redisKeyBuild
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz);


    /**
     * 根据Score值查询集合元素
     *
     * @param redisKeyBuild
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param clazz
     * @return
     */
    <T> Set<T> rangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素(含有score值), 从小到大排序
     *
     * @param redisKeyBuild
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz);


    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param redisKeyBuild
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max,
                                                                            long start, long end, Class<T> clazz);

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param redisKeyBuild
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param redisKeyBuild
     * @param min
     * @param max
     * @param clazz
     * @return
     */
    <T> Set<T> reverseRangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序, 并返回score值
     *
     * @param redisKeyBuild
     * @param min
     * @param max
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeByScoreWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param redisKeyBuild
     * @param min
     * @param max
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<T> reverseRangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, long start, long end, Class<T> clazz);

    /**
     * 根据score值获取集合元素数量
     *
     * @param redisKeyBuild
     * @param min
     * @param max
     * @return
     */
    Long countForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max);

    /**
     * 获取集合大小
     *
     * @param redisKeyBuild
     * @return
     */
    Long zCardForSortedSet(RedisKeyBuild redisKeyBuild);

    /**
     * 获取集合中value元素的score值
     *
     * @param redisKeyBuild
     * @param value
     * @return
     */
    Double scoreByValueForSortedSet(RedisKeyBuild redisKeyBuild, Object value);

    /**
     * 移除指定索引位置的成员
     *
     * @param redisKeyBuild
     * @param start
     * @param end
     * @return
     */
    Long removeRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end);

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param redisKeyBuild
     * @param min
     * @param max
     * @return
     */
    Long removeRangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max);

    /**
     * 获取key和otherKey的并集并存储在destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param destRedisKeyBuild
     * @return
     */
    Long unionAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取key和otherKeys的并集并存储在destKey中
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param destRedisKeyBuild
     * @return
     */
    Long unionAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取key和otherKey的交集并存储在destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuild
     * @param destRedisKeyBuild
     * @return
     */
    Long intersectAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild);

    /**
     * 获取key和otherKeys的交集并存储在destKey中
     *
     * @param redisKeyBuild
     * @param otherRedisKeyBuilds
     * @param destRedisKeyBuild
     * @return
     */
    Long intersectAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild);

    /**
     * 游标遍历
     * @param redisKeyBuild
     * @param options
     * @return
     */
    Cursor<ZSetOperations.TypedTuple<String>> scanForSortedSet(RedisKeyBuild redisKeyBuild, ScanOptions options);

    /**
     * 不对外使用
     * @param redisKeyBuild
     * @param genericReturnType
     * @return
     */
    <T> T getByType(RedisKeyBuild redisKeyBuild, Type genericReturnType);
    /**
     * 获取实例
     *
     * @return
     */
    RedisTemplate getInstance();
}
