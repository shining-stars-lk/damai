package com.damai;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream操作
 * @author: 阿宽不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class RedisStreamHandler {
    
    private final RedisStreamPushHandler redisStreamPushHandler;
    
    private final StringRedisTemplate stringRedisTemplate;
    
    /**
     * 用来创建绑定流和组
     */
    public void addGroup(String streamName, String groupName){
        stringRedisTemplate.opsForStream().createGroup(streamName,groupName);
    }
    
    
    /**
     * 用来判断key是否存在
     */
    public Boolean hasKey(String key){
        if(Objects.isNull(key)){
            return false;
        }else{
            return stringRedisTemplate.hasKey(key);
        }
        
    }
    /**
     * 用来删除掉消费了的消息
     */
    public void del(String key, RecordId recordIds){
        stringRedisTemplate.opsForStream().delete(key,recordIds);
    }
    
    /**
     * 用来初始化 实现绑定key和消费组
     */
    public void streamBindingGroup(String streamName, String group){
        //判断key是否存在，如果不存在则创建
        boolean hasKey = hasKey(streamName);
        if(!hasKey){
            Map<String,Object> map = new HashMap<>(2);
            map.put("key","value");
            RecordId recordId = redisStreamPushHandler.push(JSON.toJSONString(map));
            //第一次初始化时需要把Stream和group绑定
            addGroup(streamName,group);
            //清除掉该条无用数据
            del(streamName,recordId);
            log.info("initStream streamName : {} group : {}",streamName,group);
        }
    }
}
