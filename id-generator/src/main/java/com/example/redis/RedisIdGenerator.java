package com.example.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-25
 **/

public class RedisIdGenerator {
    
    private RedisTemplate redisTemplate;
    
    public RedisIdGenerator(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    
    public void id(){
        DefaultRedisScript<String> rs = new DefaultRedisScript<>("return 'Hello Redis' ", String.class);
        
        //指定传递参数序列化方式
        RedisSerializer argsSerializer = redisTemplate.getDefaultSerializer();
        //指定返回结果序列化方式
        RedisSerializer<String> resultSerializer = redisTemplate.getStringSerializer();
        
        //执行Lua脚本
        //String res = (String) redisTemplate.execute(rs, argsSerializer, resultSerializer, Arrays.asList(KYS[l], KYS[2]), ARGV[1], ARGV[1]));
    }
}
