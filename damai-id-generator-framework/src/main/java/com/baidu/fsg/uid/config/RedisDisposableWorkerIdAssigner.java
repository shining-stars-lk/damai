package com.baidu.fsg.uid.config;

import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis配置生成work_id
 * @author: 阿星不是程序员
 **/
public class RedisDisposableWorkerIdAssigner implements WorkerIdAssigner {
    
    private RedisTemplate redisTemplate;
    
    public RedisDisposableWorkerIdAssigner (RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public long assignWorkerId() {
        String key = "uid_work_id";
        Long increment = redisTemplate.opsForValue().increment(key);
        return Optional.ofNullable(increment).orElseThrow(() -> new DaMaiFrameException(BaseCode.UID_WORK_ID_ERROR));
    }
}
