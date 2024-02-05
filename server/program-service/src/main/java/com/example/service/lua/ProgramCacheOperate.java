package com.example.service.lua;

import com.example.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-22
 **/
@Slf4j
@Component
public class ProgramCacheOperate {
    
    @Autowired
    private RedisCache redisCache;
    
    private DefaultRedisScript redisScript;
    
    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/programData.lua")));
            redisScript.setResultType(Integer.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public void programCacheOperate(List<String> keys, String[] args){
        redisCache.getInstance().execute(redisScript, keys, args);
    }
}
