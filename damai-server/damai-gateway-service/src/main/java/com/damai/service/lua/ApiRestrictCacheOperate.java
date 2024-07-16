package com.damai.service.lua;

import com.alibaba.fastjson.JSON;
import com.damai.redis.RedisCache;
import com.damai.service.ApiRestrictData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: lua执行
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class ApiRestrictCacheOperate {
    
    @Autowired
    private RedisCache redisCache;
    
    private DefaultRedisScript<String> redisScript;
    
    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/apiLimit.lua")));
            redisScript.setResultType(String.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public ApiRestrictData apiRuleOperate(List<String> keys, Object[] args){
        Object object = redisCache.getInstance().execute(redisScript, keys, args);
        return JSON.parseObject((String)object, ApiRestrictData.class);
    }
}
