package com.example.save.impl;

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
import org.springframework.beans.BeanUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

public class RedisDataSave implements DataSave, EnvironmentAware {

    private final RedisCache redisCache;

    private final ApiStatProperties apiStatProperties;

    private Environment environment;

    public RedisDataSave(RedisCache redisCache,ApiStatProperties apiStatProperties){
        this.redisCache = redisCache;
        this.apiStatProperties = apiStatProperties;
    }
    @Override
    public void save(MethodHierarchyTransfer methodHierarchyTransfer) {
        MethodData currentMethodData = methodHierarchyTransfer.getCurrentMethodData();
        MethodData parentMethodData = methodHierarchyTransfer.getParentMethodData();
        BigDecimal executeTime = addMethodDetail(currentMethodData,methodHierarchyTransfer.isExceptionFlag());
        if (currentMethodData.getMethodLevel() == MethodLevel.Controller) {
            addControllerSortedSet(currentMethodData,apiStatProperties,environment,executeTime);
        }

        addChildren(parentMethodData,currentMethodData);
    }

    public BigDecimal addMethodDetail(MethodData methodData,boolean exceptionFlag){
        if (methodData == null) {
            return null;
        }
        String oldMethodDetailId = methodData.getId();
        MethodDetailData oldMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, oldMethodDetailId), MethodDetailData.class);
        if (oldMethodDetailData == null) {
            oldMethodDetailData = new MethodDetailData();
            BeanUtils.copyProperties(methodData,oldMethodDetailData);
            oldMethodDetailData.setAvgExecuteTime(methodData.getExecuteTime());
            oldMethodDetailData.setMaxExecuteTime(methodData.getExecuteTime());
            oldMethodDetailData.setMinExecuteTime(methodData.getExecuteTime());
            oldMethodDetailData.setExceptionCount(exceptionFlag ? 1L : 0L);
        }else {
            BigDecimal newAvgExecuteTime = (methodData.getExecuteTime().add(oldMethodDetailData.getAvgExecuteTime())).divide(new BigDecimal("2"),2, RoundingMode.HALF_UP);
            oldMethodDetailData.setAvgExecuteTime(newAvgExecuteTime);
            BigDecimal newMaxExecuteTime = oldMethodDetailData.getMaxExecuteTime();
            if (methodData.getExecuteTime().compareTo(oldMethodDetailData.getMaxExecuteTime()) > 0) {
                newMaxExecuteTime = methodData.getExecuteTime();
            }
            BigDecimal newMinExecuteTime = oldMethodDetailData.getMinExecuteTime();
            if (methodData.getExecuteTime().compareTo(oldMethodDetailData.getMinExecuteTime()) < 0) {
                newMinExecuteTime = methodData.getExecuteTime();
            }
            oldMethodDetailData.setMaxExecuteTime(newMaxExecuteTime);
            oldMethodDetailData.setMinExecuteTime(newMinExecuteTime);
            if (exceptionFlag) {
                oldMethodDetailData.setExceptionCount(oldMethodDetailData.getExceptionCount() + 1);
            }
        }
        redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL,oldMethodDetailId), oldMethodDetailData);
        return oldMethodDetailData.getAvgExecuteTime();
    }

    public boolean checkNoReported(MethodData methodData,ApiStatProperties apiStatProperties,Environment environment){
        String applicationName = environment.getProperty("spring.application.name");
        Map<String, String[]> noReported = apiStatProperties.getNoReported();
        String[] apis = null;
        if (!Objects.isNull(noReported)) {
            apis = noReported.get(applicationName);
        }
        if (!Objects.isNull(apis)) {
            for (String api : apis) {
                if (StringUtil.isNotEmpty(methodData.getApi()) && methodData.getApi().equals(api)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addControllerSortedSet(MethodData methodData,ApiStatProperties apiStatProperties,Environment environment,BigDecimal executeTime){
        if (methodData == null) {
            return;
        }
        if (checkNoReported(methodData,apiStatProperties,environment)) {
            return;
        }
        MethodDetailData methodDetailData = new MethodDetailData();
        BeanUtils.copyProperties(methodData,methodDetailData);
        methodDetailData.setAvgExecuteTime(executeTime);
        redisCache.addForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET),methodDetailData,executeTime.doubleValue());
    }

    public void addChildren(MethodData parentMethodData,MethodData currentMethodData){
        if (parentMethodData == null || currentMethodData == null) {
            return;
        }
        if (parentMethodData.getMethodLevel() == MethodLevel.Controller) {
            redisCache.addForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_CHILDREN_SET,parentMethodData.getId()),currentMethodData.getId());
        } else if (parentMethodData.getMethodLevel() == MethodLevel.Service) {
            redisCache.addForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_CHILDREN_SET,parentMethodData.getId()),currentMethodData.getId());
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
