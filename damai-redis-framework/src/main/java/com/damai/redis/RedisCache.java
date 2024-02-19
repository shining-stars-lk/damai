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
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: redis方法抽象
 * @author: 阿宽不是程序员
 **/
public interface RedisCache {

    /**
     * 获取字符串对象
     *
     * @param RedisKeyWrap   RedisKeyWrap
     * @param clazz 类对象
     * @param <T>   T
     * @return T 普通对象
     */
    <T> T get(RedisKeyWrap RedisKeyWrap, Class<T> clazz);
    
    /**
     * 获取字符串对象(如果缓存中不存在，则执行给定的supplier接口)
     *
     * @param RedisKeyWrap   RedisKeyWrap
     * @param clazz 类对象
     * @param <T>   T
     * @param supplier 缓存为空时，执行的逻辑
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return T 普通对象
     */
    <T> T get(RedisKeyWrap RedisKeyWrap, Class<T> clazz, Supplier<T> supplier, long ttl, TimeUnit timeUnit);

    /**
     * 返回 key 中字符串值的子字符
     * @param RedisKeyWrap
     * @param start
     * @param end
     * @return
     */
    String getRange(RedisKeyWrap RedisKeyWrap, long start, long end);

    /**
     * 获取字符串对象, 并且字符串中是集合内容
     *
     * @param RedisKeyWrap
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    <T> List<T> getValueIsList(RedisKeyWrap RedisKeyWrap, Class<T> clazz);
    
    /**
     * 获取字符串对象, 并且字符串中是集合内容(如果缓存中不存在，则执行给定的supplier接口)
     *
     * @param RedisKeyWrap
     * @param clazz
     * @param <T>
     * @param supplier 缓存为空时，执行的逻辑
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return List<T>
     */
    <T> List<T> getValueIsList(RedisKeyWrap RedisKeyWrap, Class<T> clazz, Supplier<List<T>> supplier,long ttl, TimeUnit timeUnit);


    /**
     * 通过多个key批量获取多个value
     *
     * @param keyList key集合
     * @return List<String>
     */
    List<String> getKeys(List<RedisKeyWrap> keyList);

    /**
     * 判断key是否存在
     *
     * @param RedisKeyWrap RedisKeyWrap
     * @return 是否存在 可能为空
     */
    Boolean hasKey(RedisKeyWrap RedisKeyWrap);

    /**
     * 删除key
     *
     * @param RedisKeyWrap
     * @return
     */
    void del(RedisKeyWrap RedisKeyWrap);


    /**
     * 批量删除key
     *
     * @param keys
     * @return
     */
    void del(Collection<RedisKeyWrap> keys);

    /**
     * 设置key过期时间
     *
     * @param RedisKeyWrap      RedisKeyWrap
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return 是否成功
     */
    Boolean expire(RedisKeyWrap RedisKeyWrap, long ttl, TimeUnit timeUnit);

    /**
     * 获取key超时时间
     *
     * @param RedisKeyWrap RedisKeyWrap
     * @return 超时时间
     */
    Long getExpire(RedisKeyWrap RedisKeyWrap);

    /**
     * 查找匹配的key
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param RedisKeyWrap
     * @param dbIndex
     * @return
     */
    Boolean move(RedisKeyWrap RedisKeyWrap, int dbIndex);

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param RedisKeyWrap
     * @return
     */
    Boolean persist(RedisKeyWrap RedisKeyWrap);

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return
     */
    String randomKey();

    /**
     * 修改 key 的名称
     *
     * @param oldKey
     * @param newKey
     */
    void rename(RedisKeyWrap oldKey, RedisKeyWrap newKey);

    /**
     * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    Boolean renameIfAbsent(RedisKeyWrap oldKey, RedisKeyWrap newKey);

    /**
     * 返回 key 所储存的值的类型
     *
     * @param RedisKeyWrap
     * @return
     */
    DataType type(RedisKeyWrap RedisKeyWrap);

    /**
     * 设置缓存
     *
     * @param RedisKeyWrap        缓存key
     * @param object     缓存对象
     */
    void set(RedisKeyWrap RedisKeyWrap, Object object);

    /**
     * 设置缓存
     *
     * @param RedisKeyWrap        缓存key
     * @param object     缓存对象
     * @param ttl 过期时间
     */
    void set(RedisKeyWrap RedisKeyWrap, Object object, long ttl);

    /**
     * 设置缓存
     *
     * @param RedisKeyWrap      缓存key
     * @param object   缓存对象
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    void set(RedisKeyWrap RedisKeyWrap, Object object, long ttl, TimeUnit timeUnit);

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param RedisKeyWrap
     * @param object
     * @return 之前已经存在返回false,不存在返回true
     */
    boolean setIfAbsent(RedisKeyWrap RedisKeyWrap, Object object);

    /**
     * 获取字符串的长度
     *
     * @param RedisKeyWrap
     * @return
     */
    Long size(RedisKeyWrap RedisKeyWrap);

    /**
     * 批量添加
     *
     * @param map
     */
    void multiSet(Map<RedisKeyWrap, ?> map);

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param map
     * @return 之前已经存在返回false,不存在返回true
     */
    boolean multiSetIfAbsent(Map<RedisKeyWrap, ?> map);

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param RedisKeyWrap
     * @param increment
     * @return
     */
    Long incrBy(RedisKeyWrap RedisKeyWrap, long increment);

    /**
     * double类型增加(自增长), 负数则为自减
     * @param RedisKeyWrap
     * @param increment
     * @return
     */
    Double incrByDouble(RedisKeyWrap RedisKeyWrap, double increment);

    /**
     * 追加到末尾
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Integer append(RedisKeyWrap RedisKeyWrap, String value);

    /** -------------------hash相关操作------------------------- */

    /**
     * 放置一个键值对
     *
     * @param RedisKeyWrap     hash键
     * @param hashKey hash key
     * @param value   hash value
     */
    void putHash(RedisKeyWrap RedisKeyWrap, String hashKey, Object value);

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param RedisKeyWrap           hash键
     * @param hashKey       hash key
     * @param value         hash value
     * @param ttl    过期时间
     */
    void putHash(RedisKeyWrap RedisKeyWrap, String hashKey, Object value, long ttl);

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param RedisKeyWrap       hash键
     * @param hashKey   hash key
     * @param value     hash value
     * @param ttl       过期时间
     * @param timeUnit  时间单位
     */
    void putHash(RedisKeyWrap RedisKeyWrap, String hashKey, Object value, long ttl, TimeUnit timeUnit);

    /**
     * 放入map中所有键值对
     *
     * @param RedisKeyWrap key
     * @param map hash
     */
    void putHash(RedisKeyWrap RedisKeyWrap, Map<String, ?> map);

    /**
     * 放入map中所有键值对 并设置过期时间
     *
     * @param RedisKeyWrap key
     * @param map hash
     * @param ttl 过期时间
     */
    void putHash(RedisKeyWrap RedisKeyWrap, Map<String, ?> map, long ttl);

    /**
     * 放入 Map 中所有键值对 并设置过期时间和时间单位
     *
     * @param RedisKeyWrap key
     * @param map hash
     * @param ttl 过期时间
     * @param timeUnit 时间单位
     */
    void putHash(RedisKeyWrap RedisKeyWrap, Map<String, ?> map, long ttl, TimeUnit timeUnit);

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param RedisKeyWrap
     * @param hashKey
     * @param value
     * @return
     */
    Boolean putHashIfAbsent(RedisKeyWrap RedisKeyWrap, String hashKey, Object value);

    /**
     * 从 Hash 中获取普通对象
     *
     * @param RedisKeyWrap     key
     * @param hashKey hash key
     * @param clazz   类对象
     * @param <T>     T
     * @return 普通对象
     */
    @SuppressWarnings("all")
    <T> T getForHash(RedisKeyWrap RedisKeyWrap, String hashKey, Class<T> clazz);

    /**
     * Hash的value是字符串集合，进行提取
     *
     * @param RedisKeyWrap     key
     * @param hashKey hash key
     * @param clazz   类对象
     * @param <T>     T
     * @return 普通对象
     */
    <T> List<T> getValueIsListForHash(RedisKeyWrap RedisKeyWrap, String hashKey, Class<T> clazz);

    /**
     * 从 {@code key} 处获取给定 {@code hashKeys} 的值
     *
     * @param RedisKeyWrap      key
     * @param hashKeys hashKeys
     * @param clazz    类对象
     * @param <T>      T
     * @return
     */
    <T> List<T> multiGetForHash(RedisKeyWrap RedisKeyWrap, List<String> hashKeys, Class<T> clazz);

    /**
     * 谨慎使用！
     * 获取 Hash Key 下所有值
     *
     * @param RedisKeyWrap
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getAllForHash(RedisKeyWrap RedisKeyWrap, Class<T> clazz);
    
    /**
     * 谨慎使用！
     * 获取 Hash Key 下所有值，返回值为map
     *
     * @param RedisKeyWrap
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Map<String,T> getAllMapForHash(RedisKeyWrap RedisKeyWrap, Class<T> clazz);
    /**
     * 判断hash中 key是否存在
     *
     * @param RedisKeyWrap
     * @param hashKey
     * @return
     */
    Boolean hasKeyForHash(RedisKeyWrap RedisKeyWrap, String hashKey);

    /**
     * 删除hash key
     *
     * @param RedisKeyWrap
     * @param hashKey
     * @return
     */
    Long delForHash(RedisKeyWrap RedisKeyWrap, String hashKey);

    /**
     * 批量删除hash key
     *
     * @param RedisKeyWrap
     * @param hashKeys
     * @return
     */
    Long delForHash(RedisKeyWrap RedisKeyWrap, Collection<String> hashKeys);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param RedisKeyWrap
     * @param hashKey
     * @param increment
     * @return
     */
    Long incrByForHash(RedisKeyWrap RedisKeyWrap, String hashKey, long increment);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment(double类型)
     *
     * @param RedisKeyWrap
     * @param hashKey
     * @param delta
     * @return
     */
    Double incrByDoubleForHash(RedisKeyWrap RedisKeyWrap, String hashKey, double delta);

    /**
     * 获取所有哈希表中的hashKey
     *
     * @param RedisKeyWrap
     * @return
     */
    Set<String> hashKeysForHash(RedisKeyWrap RedisKeyWrap);

    /**
     * 获取哈希表中字段的数量
     *
     * @param RedisKeyWrap
     * @return
     */
    Long sizeForHash(RedisKeyWrap RedisKeyWrap);

    /** ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param RedisKeyWrap
     * @param index
     * @param clazz
     * @return
     */
    <T> T indexForList(RedisKeyWrap RedisKeyWrap, long index, Class<T> clazz);

    /**
     * List 从左边放入元素
     *
     * @param RedisKeyWrap   key
     * @param value value
     * @return 改动行数
     */
    Long leftPushForList(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * List 从左边放入元素
     *
     * @param RedisKeyWrap   key
     * @param valueList valueList
     * @return 改动行数
     */
    Long leftPushAllForList(RedisKeyWrap RedisKeyWrap, List<Object> valueList);

    /**
     * List 从左边放入元素(当list存在的时候才加入)
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Long leftPushIfPresentForList(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * 如果pivot存在,在pivot左边添加
     *
     * @param RedisKeyWrap
     * @param pivot
     * @param value
     * @return
     */
    Long leftPushForList(RedisKeyWrap RedisKeyWrap, Object pivot, Object value);

    /**
     * List 从右边放入元素
     *
     * @param RedisKeyWrap   key
     * @param value value
     * @return 改动行数
     */
    Long rightPushForList(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * List 从右边放入元素
     *
     * @param RedisKeyWrap   key
     * @param valueList valueList
     * @return 改动行数
     */
    Long rightPushAllForList(RedisKeyWrap RedisKeyWrap, List<Object> valueList);

    /**
     * List 从右边放入元素(当list存在的时候才加入)
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Long rightPushIfPresentForList(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * 如果pivot存在,在pivot右边添加
     *
     * @param RedisKeyWrap
     * @param pivot
     * @param value
     * @return
     */
    Long rightPushForList(RedisKeyWrap RedisKeyWrap, Object pivot, Object value);

    /**
     * 通过索引设置列表元素的值
     *
     * @param RedisKeyWrap
     * @param index
     *            位置
     * @param value
     */
    void setForList(RedisKeyWrap RedisKeyWrap, long index, Object value);

    /**
     * 移出并获取列表的第一个元素
     *
     * @param RedisKeyWrap
     * @param clazz
     * @return 删除的元素
     */
    <T> T leftPopForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz);

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param RedisKeyWrap
     * @param clazz
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return
     */
    <T> T leftPopBlockForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 移除并获取列表最后一个元素
     *
     * @param RedisKeyWrap
     * @param clazz
     * @return 删除的元素
     */
    <T> T rightPopForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz);

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param RedisKeyWrap
     * @param clazz
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return
     */
    <T> T rightPopBlockForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey
     * @param destinationKey
     * @param clazz
     * @return
     */
    <T> T rightPopAndLeftPushForList(RedisKeyWrap sourceKey, RedisKeyWrap destinationKey, Class<T> clazz);

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey
     * @param destinationKey
     * @param timeout
     * @param unit
     * @return
     */
    <T> T rightPopBlockAndLeftPushForList(RedisKeyWrap sourceKey, RedisKeyWrap destinationKey, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 获取 List 全部数据
     *
     * @param RedisKeyWrap
     * @param <T>
     * @param clazz 类型
     * @return
     */
    <T> List<T> getAllForList(RedisKeyWrap RedisKeyWrap, Class<T> clazz);

    /**
     * 获取列表指定范围内的元素
     *
     * @param RedisKeyWrap
     * @param start 开始位置, 0是开始位置
     * @param end 结束位置, -1返回所有
     * @param clazz 类型
     * @return
     */
    <T> List<T> rangeForList(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz);


    /**
     * 删除集合中值等于value得元素
     *
     * @param RedisKeyWrap
     * @param index
     *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *            index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value
     * @return
     */
    Long removeForList(RedisKeyWrap RedisKeyWrap, long index, Object value);


    /**
     * 裁剪list
     *
     * @param RedisKeyWrap
     * @param start
     * @param end
     */
    void trimForList(RedisKeyWrap RedisKeyWrap, long start, long end);

    /**
     * 获取列表长度
     *
     * @param RedisKeyWrap
     * @return
     */
    Long lenForList(RedisKeyWrap RedisKeyWrap);


    /** --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Long addForSet(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * set批量添加元素
     *
     * @param RedisKeyWrap
     * @param values
     * @return
     */
    Long addForSet(RedisKeyWrap RedisKeyWrap, List<?> values);

    /**
     * set移除元素
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Long removeForSet(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * set批量移除元素
     *
     * @param RedisKeyWrap
     * @param values
     * @return
     */
    Long removeForSet(RedisKeyWrap RedisKeyWrap, List<?> values);

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param RedisKeyWrap
     * @param clazz
     * @return
     */
    <T> T popForSet(RedisKeyWrap RedisKeyWrap, Class<T> clazz);

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param RedisKeyWrap
     * @param value
     * @param destRedisKeyWrap
     * @return
     */
    boolean moveForSet(RedisKeyWrap RedisKeyWrap, Object value, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取集合的大小
     *
     * @param RedisKeyWrap
     * @return
     */
    Long sizeForSet(RedisKeyWrap RedisKeyWrap);

    /**
     * 判断集合是否包含value
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Boolean isMemberForSet(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * 获取两个集合的交集
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> intersectForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, Class<T> clazz);

    /**
     * 获取key集合与多个集合的交集
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param clazz
     * @return
     */
    <T> Set<T> intersectForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, Class<T> clazz);

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param destRedisKeyWrap
     * @return
     */
    Long intersectAndStoreForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap);

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param destRedisKeyWrap
     * @return
     */
    Long intersectAndStoreForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取两个集合的并集
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> unionForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, Class<T> clazz);

    /**
     * 获取key集合与多个集合的并集
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param clazz
     * @return
     */
    <T> Set<T> unionForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, Class<T> clazz);

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param destRedisKeyWrap
     * @return
     */
    Long unionAndStoreForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap);

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param destRedisKeyWrap
     * @return
     */
    Long unionAndStoreForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取两个集合的差集
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> differenceForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, Class<T> clazz);

    /**
     * 获取key集合与多个集合的差集
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param clazz
     * @return
     */
    <T> Set<T> differenceForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, Class<T> clazz);

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param destRedisKeyWrap
     * @return
     */
    Long differenceForSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap);

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param destRedisKeyWrap
     * @return
     */
    Long differenceForSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取集合所有元素
     *
     * @param RedisKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> membersForSet(RedisKeyWrap RedisKeyWrap, Class<T> clazz);

    /**
     * 随机获取集合中的一个元素
     *
     * @param RedisKeyWrap
     * @param clazz
     * @return
     */
    <T> T randomMemberForSet(RedisKeyWrap RedisKeyWrap, Class<T> clazz);

    /**
     * 随机获取集合中count个元素
     *
     * @param RedisKeyWrap
     * @param count
     * @param clazz
     * @return
     */
    <T> List<T> randomMembersForSet(RedisKeyWrap RedisKeyWrap, long count, Class<T> clazz);

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param RedisKeyWrap
     * @param count
     * @param clazz
     * @return
     */
    <T> Set<T> distinctRandomMembersForSet(RedisKeyWrap RedisKeyWrap, long count, Class<T> clazz);

    /**
     * 游标遍历
     * @param RedisKeyWrap
     * @param options
     * @return
     */
    Cursor<String> scanForSet(RedisKeyWrap RedisKeyWrap, ScanOptions options);



    /**------------------zSet相关操作--------------------------------*/

    /**
     * 存储有序列表
     *
     * @param RedisKeyWrap key
     * @param value value
     * @param score 评分值
     * @return
     */
    void addForZSet(RedisKeyWrap RedisKeyWrap, Object value, Double score);

    /**
     * 存储有序列表并设置超时时间(秒)
     *
     * @param RedisKeyWrap key
     * @param value value
     * @param score 评分值
     * @param ttl 超时时间
     * @return
     */
    void addForZSet(RedisKeyWrap RedisKeyWrap, Object value, Double score, long ttl);

    /**
     * 存储有序列表并设置超时时间
     *
     * @param RedisKeyWrap key
     * @param value value
     * @param score 评分值
     * @param ttl 超时时间
     * @param timeUnit 时间单位
     * @return
     */
    void addForZSet(RedisKeyWrap RedisKeyWrap, Object value, Double score, long ttl, TimeUnit timeUnit);

    /**
     * 存储有序列表
     *
     * @param RedisKeyWrap
     * @param map map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @return
     */
    Long addForZSet(RedisKeyWrap RedisKeyWrap, Map<?, Double> map);

    /**
     * 存储有序列表并设置超时时间(秒)
     *
     * @param RedisKeyWrap
     * @param map map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @param ttl
     * @return
     */
    Long addForZSet(RedisKeyWrap RedisKeyWrap, Map<?, Double> map, long ttl);

    /**
     * 存储有序列表并设置超时时间 自定义单位
     *
     * @param RedisKeyWrap      key
     * @param map      map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @param ttl      过期时间
     * @param timeUnit 过期时间单位
     * @return 影响的条目
     */
    Long addForZSet(RedisKeyWrap RedisKeyWrap, Map<?, Double> map, long ttl, TimeUnit timeUnit);

    /**
     * 获取有序列表中范围条目,并转为指定类型
     *
     * @param RedisKeyWrap   key
     * @param start 开始下标 从0开始
     * @param end   结束下标 包含此条
     * @param clazz 序列化类型
     * @param <T>   泛型参数
     * @return 结果set集合
     */
    <T> Set<T> getRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz);

    /**
     * 反转获取有序列表中范围条目,并转为指定类型
     *
     * @param RedisKeyWrap   key
     * @param start 开始下标 从0开始
     * @param end   结束下标 包含此条
     * @param clazz 序列化类型
     * @param <T>   泛型参数
     * @return 结果set集合
     */
    <T> Set<T> getReverseRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz);

    /**
     * 删除zSet条目
     *
     * @param RedisKeyWrap   key
     * @param value 数据
     * @return 影响条目
     */
    Long delForZSet(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * 批量删除zSet条目
     *
     * @param RedisKeyWrap             key
     * @param valueCollection 数据
     * @return 影响条目
     */
    Long delForZSet(RedisKeyWrap RedisKeyWrap, Collection<?> valueCollection);

    /**
     * 删除范围元素
     *
     * @param RedisKeyWrap   key
     * @param start 开始range
     * @param end   结束range
     * @return 影响条目
     */
    Long delRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end);


    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param RedisKeyWrap
     * @param value
     * @param delta
     * @return
     */
    Double incrementScoreForZSet(RedisKeyWrap RedisKeyWrap, Object value, double delta);



    /**
     * 计算zSet总条数
     *
     * @param RedisKeyWrap key
     * @return 总条数  不存在则空
     */
    Long sizeForZSet(RedisKeyWrap RedisKeyWrap);

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param RedisKeyWrap   key
     * @param value value
     * @return rank
     */
    Long rankForZSet(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Long reverseRankForZSet(RedisKeyWrap RedisKeyWrap, Object value);


    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param RedisKeyWrap
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz);


    /**
     * 根据Score值查询集合元素
     *
     * @param RedisKeyWrap
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param clazz
     * @return
     */
    <T> Set<T> rangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素(含有score值), 从小到大排序
     *
     * @param RedisKeyWrap
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz);


    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param RedisKeyWrap
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max,
                                                                       long start, long end, Class<T> clazz);

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param RedisKeyWrap
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeWithScoreForZSet(RedisKeyWrap RedisKeyWrap, long start, long end, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param RedisKeyWrap
     * @param min
     * @param max
     * @param clazz
     * @return
     */
    <T> Set<T> reverseRangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序, 并返回score值
     *
     * @param RedisKeyWrap
     * @param min
     * @param max
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeByScoreWithScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param RedisKeyWrap
     * @param min
     * @param max
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<T> reverseRangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max, long start, long end, Class<T> clazz);

    /**
     * 根据score值获取集合元素数量
     *
     * @param RedisKeyWrap
     * @param min
     * @param max
     * @return
     */
    Long countForZSet(RedisKeyWrap RedisKeyWrap, double min, double max);

    /**
     * 获取集合大小
     *
     * @param RedisKeyWrap
     * @return
     */
    Long zCardForZSet(RedisKeyWrap RedisKeyWrap);

    /**
     * 获取集合中value元素的score值
     *
     * @param RedisKeyWrap
     * @param value
     * @return
     */
    Double scoreByValueForZSet(RedisKeyWrap RedisKeyWrap, Object value);

    /**
     * 移除指定索引位置的成员
     *
     * @param RedisKeyWrap
     * @param start
     * @param end
     * @return
     */
    Long removeRangeForZSet(RedisKeyWrap RedisKeyWrap, long start, long end);

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param RedisKeyWrap
     * @param min
     * @param max
     * @return
     */
    Long removeRangeByScoreForZSet(RedisKeyWrap RedisKeyWrap, double min, double max);

    /**
     * 获取key和otherKey的并集并存储在destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param destRedisKeyWrap
     * @return
     */
    Long unionAndStoreForZSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取key和otherKeys的并集并存储在destKey中
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param destRedisKeyWrap
     * @return
     */
    Long unionAndStoreForZSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取key和otherKey的交集并存储在destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWrap
     * @param destRedisKeyWrap
     * @return
     */
    Long intersectAndStoreForZSet(RedisKeyWrap RedisKeyWrap, RedisKeyWrap otherRedisKeyWrap, RedisKeyWrap destRedisKeyWrap);

    /**
     * 获取key和otherKeys的交集并存储在destKey中
     *
     * @param RedisKeyWrap
     * @param otherRedisKeyWraps
     * @param destRedisKeyWrap
     * @return
     */
    Long intersectAndStoreForZSet(RedisKeyWrap RedisKeyWrap, Collection<RedisKeyWrap> otherRedisKeyWraps, RedisKeyWrap destRedisKeyWrap);

    /**
     * 游标遍历
     * @param RedisKeyWrap
     * @param options
     * @return
     */
    Cursor<ZSetOperations.TypedTuple<String>> scanForZSet(RedisKeyWrap RedisKeyWrap, ScanOptions options);

    /**
     * 不对外使用
     * @param RedisKeyWrap
     * @param genericReturnType
     * @return
     */
    <T> T getByType(RedisKeyWrap RedisKeyWrap, Type genericReturnType);
    /**
     * 获取实例
     *
     * @return
     */
    RedisTemplate getInstance();
}
