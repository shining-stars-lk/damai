package com.example.distributecache.redis;

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

/**
 * @program: distribute-cache
 * @description: 分布式缓存统一接口
 * @author: lk
 * @create: 2022-05-28
 **/
public interface DistributCache {

    /**
     * 获取字符串对象
     *
     * @param cacheKeyWrap   cacheKeyWrap
     * @param clazz 类对象
     * @param <T>   T
     * @return T 普通对象
     */
    <T> T get(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 返回 key 中字符串值的子字符
     * @param cacheKeyWrap
     * @param start
     * @param end
     * @return
     */
    String getRange(CacheKeyWrap cacheKeyWrap, long start, long end);

    /**
     * 获取字符串对象, 并且字符串中是集合内容
     *
     * @param cacheKeyWrap
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    <T> List<T> getValueIsList(CacheKeyWrap cacheKeyWrap, Class<T> clazz);


    /**
     * 通过多个key批量获取多个value
     *
     * @param keyList key集合
     * @return List<String>
     */
    List<String> getKeys(List<CacheKeyWrap> keyList);

    /**
     * 判断key是否存在
     *
     * @param cacheKeyWrap cacheKeyWrap
     * @return 是否存在 可能为空
     */
    Boolean hasKey(CacheKeyWrap cacheKeyWrap);

    /**
     * 删除key
     *
     * @param cacheKeyWrap
     * @return
     */
    void del(CacheKeyWrap cacheKeyWrap);


    /**
     * 批量删除key
     *
     * @param keys
     * @return
     */
    void del(Collection<CacheKeyWrap> keys);

    /**
     * 设置key过期时间
     *
     * @param cacheKeyWrap      cacheKeyWrap
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     * @return 是否成功
     */
    Boolean expire(CacheKeyWrap cacheKeyWrap, long ttl, TimeUnit timeUnit);

    /**
     * 获取key超时时间
     *
     * @param cacheKeyWrap cacheKeyWrap
     * @return 超时时间
     */
    Long getExpire(CacheKeyWrap cacheKeyWrap);

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
     * @param cacheKeyWrap
     * @param dbIndex
     * @return
     */
    Boolean move(CacheKeyWrap cacheKeyWrap, int dbIndex);

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param cacheKeyWrap
     * @return
     */
    Boolean persist(CacheKeyWrap cacheKeyWrap);

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
    void rename(CacheKeyWrap oldKey, CacheKeyWrap newKey);

    /**
     * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    Boolean renameIfAbsent(CacheKeyWrap oldKey, CacheKeyWrap newKey);

    /**
     * 返回 key 所储存的值的类型
     *
     * @param cacheKeyWrap
     * @return
     */
    DataType type(CacheKeyWrap cacheKeyWrap);

    /**
     * 设置缓存
     *
     * @param cacheKeyWrap        缓存key
     * @param object     缓存对象
     */
    void set(CacheKeyWrap cacheKeyWrap, Object object);

    /**
     * 设置缓存
     *
     * @param cacheKeyWrap        缓存key
     * @param object     缓存对象
     * @param ttl 过期时间
     */
    void set(CacheKeyWrap cacheKeyWrap, Object object, long ttl);

    /**
     * 设置缓存
     *
     * @param cacheKeyWrap      缓存key
     * @param object   缓存对象
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    void set(CacheKeyWrap cacheKeyWrap, Object object, long ttl, TimeUnit timeUnit);

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param cacheKeyWrap
     * @param object
     * @return 之前已经存在返回false,不存在返回true
     */
    boolean setIfAbsent(CacheKeyWrap cacheKeyWrap, Object object);

    /**
     * 获取字符串的长度
     *
     * @param cacheKeyWrap
     * @return
     */
    Long size(CacheKeyWrap cacheKeyWrap);

    /**
     * 批量添加
     *
     * @param map
     */
    void multiSet(Map<CacheKeyWrap, ?> map);

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param map
     * @return 之前已经存在返回false,不存在返回true
     */
    boolean multiSetIfAbsent(Map<CacheKeyWrap, ?> map);

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param cacheKeyWrap
     * @param increment
     * @return
     */
    Long incrBy(CacheKeyWrap cacheKeyWrap, long increment);

    /**
     * double类型增加(自增长), 负数则为自减
     * @param cacheKeyWrap
     * @param increment
     * @return
     */
    Double incrByDouble(CacheKeyWrap cacheKeyWrap, double increment);

    /**
     * 追加到末尾
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Integer append(CacheKeyWrap cacheKeyWrap, String value);

    /** -------------------hash相关操作------------------------- */

    /**
     * 放置一个键值对
     *
     * @param cacheKeyWrap     hash键
     * @param hashKey hash key
     * @param value   hash value
     */
    void putHash(CacheKeyWrap cacheKeyWrap, String hashKey, Object value);

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param cacheKeyWrap           hash键
     * @param hashKey       hash key
     * @param value         hash value
     * @param ttl    过期时间
     */
    void putHash(CacheKeyWrap cacheKeyWrap, String hashKey, Object value, long ttl);

    /**
     * 放置一个键值对 并设置过期时间
     *
     * @param cacheKeyWrap       hash键
     * @param hashKey   hash key
     * @param value     hash value
     * @param ttl       过期时间
     * @param timeUnit  时间单位
     */
    void putHash(CacheKeyWrap cacheKeyWrap, String hashKey, Object value, long ttl, TimeUnit timeUnit);

    /**
     * 放入map中所有键值对
     *
     * @param cacheKeyWrap key
     * @param map hash
     */
    void putHash(CacheKeyWrap cacheKeyWrap, Map<String, ?> map);

    /**
     * 放入map中所有键值对 并设置过期时间
     *
     * @param cacheKeyWrap key
     * @param map hash
     * @param ttl 过期时间
     */
    void putHash(CacheKeyWrap cacheKeyWrap, Map<String, ?> map, long ttl);

    /**
     * 放入 Map 中所有键值对 并设置过期时间和时间单位
     *
     * @param cacheKeyWrap key
     * @param map hash
     * @param ttl 过期时间
     * @param timeUnit 时间单位
     */
    void putHash(CacheKeyWrap cacheKeyWrap, Map<String, ?> map, long ttl, TimeUnit timeUnit);

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param cacheKeyWrap
     * @param hashKey
     * @param value
     * @return
     */
    Boolean putHashIfAbsent(CacheKeyWrap cacheKeyWrap, String hashKey, Object value);

    /**
     * 从 Hash 中获取普通对象
     *
     * @param cacheKeyWrap     key
     * @param hashKey hash key
     * @param clazz   类对象
     * @param <T>     T
     * @return 普通对象
     */
    @SuppressWarnings("all")
    <T> T getForHash(CacheKeyWrap cacheKeyWrap, String hashKey, Class<T> clazz);

    /**
     * Hash的value是字符串集合，进行提取
     *
     * @param cacheKeyWrap     key
     * @param hashKey hash key
     * @param clazz   类对象
     * @param <T>     T
     * @return 普通对象
     */
    <T> List<T> getValueIsListForHash(CacheKeyWrap cacheKeyWrap, String hashKey, Class<T> clazz);

    /**
     * 从 {@code key} 处获取给定 {@code hashKeys} 的值
     *
     * @param cacheKeyWrap      key
     * @param hashKeys hashKeys
     * @param clazz    类对象
     * @param <T>      T
     * @return
     */
    <T> List<T> multiGetForHash(CacheKeyWrap cacheKeyWrap, List<String> hashKeys, Class<T> clazz);

    /**
     * 谨慎使用！
     * 获取 Hash Key 下所有值
     *
     * @param cacheKeyWrap
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> getAllForHash(CacheKeyWrap cacheKeyWrap, Class<T> clazz);


    /**
     * 判断hash中 key是否存在
     *
     * @param cacheKeyWrap
     * @param hashKey
     * @return
     */
    Boolean hasKeyForHash(CacheKeyWrap cacheKeyWrap, String hashKey);

    /**
     * 删除hash key
     *
     * @param cacheKeyWrap
     * @param hashKey
     * @return
     */
    Long delForHash(CacheKeyWrap cacheKeyWrap, String hashKey);

    /**
     * 批量删除hash key
     *
     * @param cacheKeyWrap
     * @param hashKeys
     * @return
     */
    Long delForHash(CacheKeyWrap cacheKeyWrap, Collection<String> hashKeys);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param cacheKeyWrap
     * @param hashKey
     * @param increment
     * @return
     */
    Long incrByForHash(CacheKeyWrap cacheKeyWrap, String hashKey, long increment);

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment(double类型)
     *
     * @param cacheKeyWrap
     * @param hashKey
     * @param delta
     * @return
     */
    Double incrByDoubleForHash(CacheKeyWrap cacheKeyWrap, String hashKey, double delta);

    /**
     * 获取所有哈希表中的hashKey
     *
     * @param cacheKeyWrap
     * @return
     */
    Set<String> hashKeysForHash(CacheKeyWrap cacheKeyWrap);

    /**
     * 获取哈希表中字段的数量
     *
     * @param cacheKeyWrap
     * @return
     */
    Long sizeForHash(CacheKeyWrap cacheKeyWrap);

    /** ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param cacheKeyWrap
     * @param index
     * @param clazz
     * @return
     */
    <T> T indexForList(CacheKeyWrap cacheKeyWrap, long index, Class<T> clazz);

    /**
     * List 从左边放入元素
     *
     * @param cacheKeyWrap   key
     * @param value value
     * @return 改动行数
     */
    Long leftPushForList(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * List 从左边放入元素
     *
     * @param cacheKeyWrap   key
     * @param valueList valueList
     * @return 改动行数
     */
    Long leftPushAllForList(CacheKeyWrap cacheKeyWrap, List<Object> valueList);

    /**
     * List 从左边放入元素(当list存在的时候才加入)
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Long leftPushIfPresentForList(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * 如果pivot存在,在pivot左边添加
     *
     * @param cacheKeyWrap
     * @param pivot
     * @param value
     * @return
     */
    Long leftPushForList(CacheKeyWrap cacheKeyWrap, Object pivot, Object value);

    /**
     * List 从右边放入元素
     *
     * @param cacheKeyWrap   key
     * @param value value
     * @return 改动行数
     */
    Long rightPushForList(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * List 从右边放入元素
     *
     * @param cacheKeyWrap   key
     * @param valueList valueList
     * @return 改动行数
     */
    Long rightPushAllForList(CacheKeyWrap cacheKeyWrap, List<Object> valueList);

    /**
     * List 从右边放入元素(当list存在的时候才加入)
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Long rightPushIfPresentForList(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * 如果pivot存在,在pivot右边添加
     *
     * @param cacheKeyWrap
     * @param pivot
     * @param value
     * @return
     */
    Long rightPushForList(CacheKeyWrap cacheKeyWrap, Object pivot, Object value);

    /**
     * 通过索引设置列表元素的值
     *
     * @param cacheKeyWrap
     * @param index
     *            位置
     * @param value
     */
    void setForList(CacheKeyWrap cacheKeyWrap, long index, Object value);

    /**
     * 移出并获取列表的第一个元素
     *
     * @param cacheKeyWrap
     * @param clazz
     * @return 删除的元素
     */
    <T> T leftPopForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param cacheKeyWrap
     * @param clazz
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return
     */
    <T> T leftPopBlockForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 移除并获取列表最后一个元素
     *
     * @param cacheKeyWrap
     * @param clazz
     * @return 删除的元素
     */
    <T> T rightPopForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param cacheKeyWrap
     * @param clazz
     * @param timeout
     *            等待时间
     * @param unit
     *            时间单位
     * @return
     */
    <T> T rightPopBlockForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey
     * @param destinationKey
     * @param clazz
     * @return
     */
    <T> T rightPopAndLeftPushForList(CacheKeyWrap sourceKey, CacheKeyWrap destinationKey, Class<T> clazz);

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey
     * @param destinationKey
     * @param timeout
     * @param unit
     * @return
     */
    <T> T rightPopBlockAndLeftPushForList(CacheKeyWrap sourceKey, CacheKeyWrap destinationKey, Class<T> clazz, long timeout, TimeUnit unit);

    /**
     * 获取 List 全部数据
     *
     * @param cacheKeyWrap
     * @param <T>
     * @param clazz 类型
     * @return
     */
    <T> List<T> getAllForList(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 获取列表指定范围内的元素
     *
     * @param cacheKeyWrap
     * @param start 开始位置, 0是开始位置
     * @param end 结束位置, -1返回所有
     * @param clazz 类型
     * @return
     */
    <T> List<T> rangeForList(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz);


    /**
     * 删除集合中值等于value得元素
     *
     * @param cacheKeyWrap
     * @param index
     *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *            index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value
     * @return
     */
    Long removeForList(CacheKeyWrap cacheKeyWrap, long index, Object value);


    /**
     * 裁剪list
     *
     * @param cacheKeyWrap
     * @param start
     * @param end
     */
    void trimForList(CacheKeyWrap cacheKeyWrap, long start, long end);

    /**
     * 获取列表长度
     *
     * @param cacheKeyWrap
     * @return
     */
    Long lenForList(CacheKeyWrap cacheKeyWrap);


    /** --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Long addForSet(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * set批量添加元素
     *
     * @param cacheKeyWrap
     * @param values
     * @return
     */
    Long addForSet(CacheKeyWrap cacheKeyWrap, List<?> values);

    /**
     * set移除元素
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Long removeForSet(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * set批量移除元素
     *
     * @param cacheKeyWrap
     * @param values
     * @return
     */
    Long removeForSet(CacheKeyWrap cacheKeyWrap, List<?> values);

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param cacheKeyWrap
     * @param clazz
     * @return
     */
    <T> T popForSet(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param cacheKeyWrap
     * @param value
     * @param destCacheKeyWrap
     * @return
     */
    boolean moveForSet(CacheKeyWrap cacheKeyWrap, Object value, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取集合的大小
     *
     * @param cacheKeyWrap
     * @return
     */
    Long sizeForSet(CacheKeyWrap cacheKeyWrap);

    /**
     * 判断集合是否包含value
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Boolean isMemberForSet(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * 获取两个集合的交集
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> intersectForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, Class<T> clazz);

    /**
     * 获取key集合与多个集合的交集
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param clazz
     * @return
     */
    <T> Set<T> intersectForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, Class<T> clazz);

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param destCacheKeyWrap
     * @return
     */
    Long intersectAndStoreForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap);

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param destCacheKeyWrap
     * @return
     */
    Long intersectAndStoreForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取两个集合的并集
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> unionForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, Class<T> clazz);

    /**
     * 获取key集合与多个集合的并集
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param clazz
     * @return
     */
    <T> Set<T> unionForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, Class<T> clazz);

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param destCacheKeyWrap
     * @return
     */
    Long unionAndStoreForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap);

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param destCacheKeyWrap
     * @return
     */
    Long unionAndStoreForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取两个集合的差集
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> differenceForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, Class<T> clazz);

    /**
     * 获取key集合与多个集合的差集
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param clazz
     * @return
     */
    <T> Set<T> differenceForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, Class<T> clazz);

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param destCacheKeyWrap
     * @return
     */
    Long differenceForSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap);

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param destCacheKeyWrap
     * @return
     */
    Long differenceForSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取集合所有元素
     *
     * @param cacheKeyWrap
     * @param clazz
     * @return
     */
    <T> Set<T> membersForSet(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 随机获取集合中的一个元素
     *
     * @param cacheKeyWrap
     * @param clazz
     * @return
     */
    <T> T randomMemberForSet(CacheKeyWrap cacheKeyWrap, Class<T> clazz);

    /**
     * 随机获取集合中count个元素
     *
     * @param cacheKeyWrap
     * @param count
     * @param clazz
     * @return
     */
    <T> List<T> randomMembersForSet(CacheKeyWrap cacheKeyWrap, long count, Class<T> clazz);

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param cacheKeyWrap
     * @param count
     * @param clazz
     * @return
     */
    <T> Set<T> distinctRandomMembersForSet(CacheKeyWrap cacheKeyWrap, long count, Class<T> clazz);

    /**
     * 游标遍历
     * @param cacheKeyWrap
     * @param options
     * @return
     */
    Cursor<String> scanForSet(CacheKeyWrap cacheKeyWrap, ScanOptions options);



    /**------------------zSet相关操作--------------------------------*/

    /**
     * 存储有序列表
     *
     * @param cacheKeyWrap key
     * @param value value
     * @param score 评分值
     * @return
     */
    void addForZSet(CacheKeyWrap cacheKeyWrap, Object value, Double score);

    /**
     * 存储有序列表并设置超时时间(秒)
     *
     * @param cacheKeyWrap key
     * @param value value
     * @param score 评分值
     * @param ttl 超时时间
     * @return
     */
    void addForZSet(CacheKeyWrap cacheKeyWrap, Object value, Double score, long ttl);

    /**
     * 存储有序列表并设置超时时间
     *
     * @param cacheKeyWrap key
     * @param value value
     * @param score 评分值
     * @param ttl 超时时间
     * @param timeUnit 时间单位
     * @return
     */
    void addForZSet(CacheKeyWrap cacheKeyWrap, Object value, Double score, long ttl, TimeUnit timeUnit);

    /**
     * 存储有序列表
     *
     * @param cacheKeyWrap
     * @param map map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @return
     */
    Long addForZSet(CacheKeyWrap cacheKeyWrap, Map<?, Double> map);

    /**
     * 存储有序列表并设置超时时间(秒)
     *
     * @param cacheKeyWrap
     * @param map map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @param ttl
     * @return
     */
    Long addForZSet(CacheKeyWrap cacheKeyWrap, Map<?, Double> map, long ttl);

    /**
     * 存储有序列表并设置超时时间 自定义单位
     *
     * @param cacheKeyWrap      key
     * @param map      map中的key如果是自定义对象类型需要重新equals和hashcode方法
     * @param ttl      过期时间
     * @param timeUnit 过期时间单位
     * @return 影响的条目
     */
    Long addForZSet(CacheKeyWrap cacheKeyWrap, Map<?, Double> map, long ttl, TimeUnit timeUnit);

    /**
     * 获取有序列表中范围条目,并转为指定类型
     *
     * @param cacheKeyWrap   key
     * @param start 开始下标 从0开始
     * @param end   结束下标 包含此条
     * @param clazz 序列化类型
     * @param <T>   泛型参数
     * @return 结果set集合
     */
    <T> Set<T> getRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz);

    /**
     * 反转获取有序列表中范围条目,并转为指定类型
     *
     * @param cacheKeyWrap   key
     * @param start 开始下标 从0开始
     * @param end   结束下标 包含此条
     * @param clazz 序列化类型
     * @param <T>   泛型参数
     * @return 结果set集合
     */
    <T> Set<T> getReverseRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz);

    /**
     * 删除zSet条目
     *
     * @param cacheKeyWrap   key
     * @param value 数据
     * @return 影响条目
     */
    Long delForZSet(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * 批量删除zSet条目
     *
     * @param cacheKeyWrap             key
     * @param valueCollection 数据
     * @return 影响条目
     */
    Long delForZSet(CacheKeyWrap cacheKeyWrap, Collection<?> valueCollection);

    /**
     * 删除范围元素
     *
     * @param cacheKeyWrap   key
     * @param start 开始range
     * @param end   结束range
     * @return 影响条目
     */
    Long delRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end);


    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param cacheKeyWrap
     * @param value
     * @param delta
     * @return
     */
    Double incrementScoreForZSet(CacheKeyWrap cacheKeyWrap, Object value, double delta);



    /**
     * 计算zSet总条数
     *
     * @param cacheKeyWrap key
     * @return 总条数  不存在则空
     */
    Long sizeForZSet(CacheKeyWrap cacheKeyWrap);

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param cacheKeyWrap   key
     * @param value value
     * @return rank
     */
    Long rankForZSet(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Long reverseRankForZSet(CacheKeyWrap cacheKeyWrap, Object value);


    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param cacheKeyWrap
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz);


    /**
     * 根据Score值查询集合元素
     *
     * @param cacheKeyWrap
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param clazz
     * @return
     */
    <T> Set<T> rangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素(含有score值), 从小到大排序
     *
     * @param cacheKeyWrap
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz);


    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param cacheKeyWrap
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max,
                                                                       long start, long end, Class<T> clazz);

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param cacheKeyWrap
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeWithScoreForZSet(CacheKeyWrap cacheKeyWrap, long start, long end, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param cacheKeyWrap
     * @param min
     * @param max
     * @param clazz
     * @return
     */
    <T> Set<T> reverseRangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序, 并返回score值
     *
     * @param cacheKeyWrap
     * @param min
     * @param max
     * @param clazz
     * @return
     */
    <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeByScoreWithScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, Class<T> clazz);

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param cacheKeyWrap
     * @param min
     * @param max
     * @param start
     * @param end
     * @param clazz
     * @return
     */
    <T> Set<T> reverseRangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max, long start, long end, Class<T> clazz);

    /**
     * 根据score值获取集合元素数量
     *
     * @param cacheKeyWrap
     * @param min
     * @param max
     * @return
     */
    Long countForZSet(CacheKeyWrap cacheKeyWrap, double min, double max);

    /**
     * 获取集合大小
     *
     * @param cacheKeyWrap
     * @return
     */
    Long zCardForZSet(CacheKeyWrap cacheKeyWrap);

    /**
     * 获取集合中value元素的score值
     *
     * @param cacheKeyWrap
     * @param value
     * @return
     */
    Double scoreByValueForZSet(CacheKeyWrap cacheKeyWrap, Object value);

    /**
     * 移除指定索引位置的成员
     *
     * @param cacheKeyWrap
     * @param start
     * @param end
     * @return
     */
    Long removeRangeForZSet(CacheKeyWrap cacheKeyWrap, long start, long end);

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param cacheKeyWrap
     * @param min
     * @param max
     * @return
     */
    Long removeRangeByScoreForZSet(CacheKeyWrap cacheKeyWrap, double min, double max);

    /**
     * 获取key和otherKey的并集并存储在destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param destCacheKeyWrap
     * @return
     */
    Long unionAndStoreForZSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取key和otherKeys的并集并存储在destKey中
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param destCacheKeyWrap
     * @return
     */
    Long unionAndStoreForZSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取key和otherKey的交集并存储在destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWrap
     * @param destCacheKeyWrap
     * @return
     */
    Long intersectAndStoreForZSet(CacheKeyWrap cacheKeyWrap, CacheKeyWrap otherCacheKeyWrap, CacheKeyWrap destCacheKeyWrap);

    /**
     * 获取key和otherKeys的交集并存储在destKey中
     *
     * @param cacheKeyWrap
     * @param otherCacheKeyWraps
     * @param destCacheKeyWrap
     * @return
     */
    Long intersectAndStoreForZSet(CacheKeyWrap cacheKeyWrap, Collection<CacheKeyWrap> otherCacheKeyWraps, CacheKeyWrap destCacheKeyWrap);

    /**
     * 游标遍历
     * @param cacheKeyWrap
     * @param options
     * @return
     */
    Cursor<ZSetOperations.TypedTuple<String>> scanForZSet(CacheKeyWrap cacheKeyWrap, ScanOptions options);

    /**
     * 不对外使用
     * @param cacheKeyWrap
     * @param genericReturnType
     * @return
     */
    <T> T getByType(CacheKeyWrap cacheKeyWrap, Type genericReturnType);
    /**
     * 获取实例
     *
     * @return
     */
    RedisTemplate getInstance();
}
