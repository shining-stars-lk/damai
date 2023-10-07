package com.example.save.impl;


import com.alibaba.fastjson.JSON;
import com.example.common.ApiStatCommon;
import com.example.config.ApiStatProperties;
import com.example.core.RedisKeyEnum;
import com.example.core.StringUtil;
import com.example.enums.MethodLevel;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.save.DataSave;
import com.example.structure.MethodData;
import com.example.structure.MethodDetailData;
import com.example.structure.MethodHierarchyTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.constant.ApiStatConstant.REDIS_DATA_SAVE_LUA_PATH;

@Slf4j
public class RedisDataSave implements DataSave {

    private final RedisCache redisCache;

    private final ApiStatProperties apiStatProperties;
    
    private final ApiStatCommon apiStatCommon;

    private final PathMatcher matcher = new AntPathMatcher();

    private DefaultRedisScript redisScript;

    public RedisDataSave(RedisCache redisCache,ApiStatProperties apiStatProperties,ApiStatCommon apiStatCommon){
        this.redisCache = redisCache;
        this.apiStatProperties = apiStatProperties;
        this.apiStatCommon = apiStatCommon;
    }

    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(REDIS_DATA_SAVE_LUA_PATH)));
            redisScript.setResultType(String.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }

    @Override
    public void save(MethodHierarchyTransfer methodHierarchyTransfer) {
        MethodData currentMethodData = methodHierarchyTransfer.getCurrentMethodData();
        MethodData parentMethodData = methodHierarchyTransfer.getParentMethodData();
        BigDecimal executeTime = addMethodDetail(currentMethodData,methodHierarchyTransfer.isExceptionFlag());
        if (currentMethodData.getMethodLevel() == MethodLevel.CONTROLLER) {
            addControllerSortedSet(currentMethodData,apiStatProperties,executeTime);
        }

        addChildren(parentMethodData,currentMethodData);
    }

    public BigDecimal addMethodDetail(MethodData methodData,boolean exceptionFlag){
        if (methodData == null) {
            return null;
        }
        String oldMethodDetailIdRelKey = RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, methodData.getId()).getRelKey();
        List<String> keys = Stream.of(oldMethodDetailIdRelKey).collect(Collectors.toList());
        String[] parameters = new String[]{JSON.toJSONString(methodData),exceptionFlag ? "1" : "0"};
        String avgExecuteTime = (String)redisCache.getInstance().execute(redisScript, keys, parameters);
        return new BigDecimal(avgExecuteTime);
    }

    public boolean checkNoReported(MethodData methodData,ApiStatProperties apiStatProperties){
        
        Map<String, String[]> noReported = apiStatProperties.getNoReported();
        String[] apis = null;
        if (!Objects.isNull(noReported)) {
            apis = noReported.get(apiStatCommon.getApplicationName());
        }
        if (!Objects.isNull(apis)) {
            for (String api : apis) {
                if (StringUtil.isNotEmpty(methodData.getApi()) && matcher.match(api,methodData.getApi())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addControllerSortedSet(MethodData methodData,ApiStatProperties apiStatProperties,BigDecimal executeTime){
        if (methodData == null) {
            return;
        }
        if (checkNoReported(methodData,apiStatProperties)) {
            return;
        }
        MethodDetailData methodDetailData = new MethodDetailData();
        BeanUtils.copyProperties(methodData,methodDetailData);
        methodDetailData.setExecuteTime(null);
        methodDetailData.setExceptionCount(0L);
        redisCache.addForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET),methodDetailData,executeTime.doubleValue());
    }

    public void addChildren(MethodData parentMethodData,MethodData currentMethodData){
        if (parentMethodData == null || currentMethodData == null) {
            return;
        }
        if (parentMethodData.getMethodLevel() == MethodLevel.CONTROLLER) {
            redisCache.addForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_CHILDREN_SET,parentMethodData.getId()),currentMethodData.getId());
        } else if (parentMethodData.getMethodLevel() == MethodLevel.SERVICE) {
            redisCache.addForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_CHILDREN_SET,parentMethodData.getId()),currentMethodData.getId());
        }
    }
}
