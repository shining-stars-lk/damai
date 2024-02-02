package com.example.toolkit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-02
 **/
@Slf4j
public class WorkAndDataCenterIdHandler {
    
    private final String SNOWFLAKE_ID_HASH = "snowflake_id_hash";
    
    private final String SNOWFLAKE_WORK_ID_HASH_KEY = "snowflake_work_id_hash_key";
    
    private final String SNOWFLAKE_DATA_CENTER_ID_HASH_KEY = "snowflake_data_center_id_hash_key";
    
    public final List<String> keys = Stream.of(SNOWFLAKE_ID_HASH,SNOWFLAKE_WORK_ID_HASH_KEY,SNOWFLAKE_DATA_CENTER_ID_HASH_KEY)
            .collect(Collectors.toList());
    
    private StringRedisTemplate stringRedisTemplate;
    
    private DefaultRedisScript redisScript;
    
    public WorkAndDataCenterIdHandler(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
        try {
            redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/ProgramData.lua")));
            redisScript.setResultType(Integer.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public void getWorkAndDataCenterId(){
        String[] data = new String[2];
        data[0] = String.valueOf(IdGeneratorConstant.MAX_WORKER_ID);
        data[1] = String.valueOf(IdGeneratorConstant.MAX_DATA_CENTER_ID);
        stringRedisTemplate.execute(redisScript, keys, data);
    }
}
