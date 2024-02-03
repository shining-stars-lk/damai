package com.example.toolkit;

import com.alibaba.fastjson.JSON;
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
    
    private final String SNOWFLAKE_WORK_ID_KEY = "snowflake_work_id";
    
    private final String SNOWFLAKE_DATA_CENTER_ID_key = "snowflake_data_center_id";
    
    
    public final List<String> keys = Stream.of(SNOWFLAKE_WORK_ID_KEY,SNOWFLAKE_DATA_CENTER_ID_key).collect(Collectors.toList());
    
    private StringRedisTemplate stringRedisTemplate;
    
    private DefaultRedisScript<String> redisScript;
    
    public WorkAndDataCenterIdHandler(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
        try {
            redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/workAndDataCenterId.lua")));
            redisScript.setResultType(String.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
    public WorkDataCenterId getWorkAndDataCenterId(){
        WorkDataCenterId workDataCenterId = new WorkDataCenterId();
        try {
            String[] data = new String[2];
            data[0] = String.valueOf(IdGeneratorConstant.MAX_WORKER_ID);
            data[1] = String.valueOf(IdGeneratorConstant.MAX_DATA_CENTER_ID);
            String result = stringRedisTemplate.execute(redisScript, keys, data);
            workDataCenterId = JSON.parseObject(result,WorkDataCenterId.class);
        }catch (Exception e) {
            log.error("getWorkAndDataCenterId error",e);
        }
        return workDataCenterId;
    }
}
