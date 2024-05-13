package com.damai.service.lua;

import com.damai.initialize.base.AbstractApplicationPostConstructHandler;
import com.damai.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 判断是否需要进行校验验证码
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class CheckNeedCaptchaOperate extends AbstractApplicationPostConstructHandler {
    
    @Autowired
    private RedisCache redisCache;
    
    private DefaultRedisScript<String> redisScript;
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
    
    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        try {
            redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/checkNeedCaptcha.lua")));
            redisScript.setResultType(String.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public Boolean checkNeedCaptchaOperate(List<String> keys, String[] args){
        Object object = redisCache.getInstance().execute(redisScript, keys, args);
        return Boolean.parseBoolean((String)object);
    }
}
