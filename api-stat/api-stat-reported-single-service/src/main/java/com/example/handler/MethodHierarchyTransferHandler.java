package com.example.handler;

import com.example.core.RedisKeyEnum;
import com.example.enums.MethodLevel;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.structure.MethodData;
import com.example.structure.MethodDetailData;
import com.example.structure.MethodHierarchy;
import com.example.structure.MethodHierarchyTransfer;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.constant.ApiStatConstant.METHOD_DATA_SPLIT;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@AllArgsConstructor
public class MethodHierarchyTransferHandler {
    
    private final RedisCache redisCache;
    
    public void consumer(MethodHierarchyTransfer methodHierarchyTransfer){
        MethodData currentMethodData = methodHierarchyTransfer.getCurrentMethodData();
        MethodData parentMethodData = methodHierarchyTransfer.getParentMethodData();
        BigDecimal executeTime = addMethodDetail(currentMethodData,methodHierarchyTransfer.isExceptionFlag());
        if (currentMethodData.getMethodLevel() == MethodLevel.Controller) {
            addControllerSortedSet(currentMethodData,executeTime);
        }

        addChildren(parentMethodData,currentMethodData);
    }
    
//    public void addMethodData(MethodData methodData){
//        if (methodData == null) {
//            return;
//        }
//        if (methodData.getMethodLevel() == MethodLevel.Controller) {
//            redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_METHOD_DATA,methodData.getId()),methodData);
//        } else if (methodData.getMethodLevel() == MethodLevel.Service) {
//            redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_METHOD_DATA,methodData.getId()),methodData);
//        } else if (methodData.getMethodLevel() == MethodLevel.Dao) {
//            redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_DAO_METHOD_DATA,methodData.getId()),methodData);
//        }
//    }
//
//    public BigDecimal addMethodHierarchy(MethodData parentMethodData,MethodData currentMethodData,boolean exceptionFlag){
//        if (parentMethodData == null || currentMethodData == null) {
//            return null;
//        }
//        String oldMethodHierarchyId = parentMethodData.getId() + METHOD_DATA_SPLIT +currentMethodData.getId();
//        MethodHierarchy oldMethodHierarchy = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_HIERARCHY, oldMethodHierarchyId), MethodHierarchy.class);
//        if (oldMethodHierarchy == null) {
//            oldMethodHierarchy = new MethodHierarchy();
//            oldMethodHierarchy.setParentMethodDataId(parentMethodData.getId());
//            oldMethodHierarchy.setCurrentMethodDataId(currentMethodData.getId());
//            oldMethodHierarchy.setId(oldMethodHierarchyId);
//            oldMethodHierarchy.setAvgExecuteTime(currentMethodData.getRunTime());
//            oldMethodHierarchy.setMaxExecuteTime(currentMethodData.getRunTime());
//            oldMethodHierarchy.setMinExecuteTime(currentMethodData.getRunTime());
//            oldMethodHierarchy.setExceptionCount(exceptionFlag ? 1L : 0L);
//        }else {
//            BigDecimal newAvgExecuteTime = (currentMethodData.getRunTime().add(oldMethodHierarchy.getAvgExecuteTime())).divide(new BigDecimal("2"),2,RoundingMode.HALF_UP);
//            oldMethodHierarchy.setAvgExecuteTime(newAvgExecuteTime);
//            BigDecimal newMaxExecuteTime = oldMethodHierarchy.getMaxExecuteTime();
//            if (currentMethodData.getRunTime().compareTo(oldMethodHierarchy.getMaxExecuteTime()) > 0) {
//                newMaxExecuteTime = currentMethodData.getRunTime();
//            }
//            BigDecimal newMinExecuteTime = oldMethodHierarchy.getMinExecuteTime();
//            if (currentMethodData.getRunTime().compareTo(oldMethodHierarchy.getMinExecuteTime()) < 0) {
//                newMinExecuteTime = currentMethodData.getRunTime();
//            }
//            oldMethodHierarchy.setMaxExecuteTime(newMaxExecuteTime);
//            oldMethodHierarchy.setMinExecuteTime(newMinExecuteTime);
//            if (exceptionFlag) {
//                oldMethodHierarchy.setExceptionCount(oldMethodHierarchy.getExceptionCount() + 1);
//            }
//        }
//        redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_HIERARCHY,oldMethodHierarchyId), oldMethodHierarchy);
//        return oldMethodHierarchy.getAvgExecuteTime();
//    }

    public BigDecimal addMethodDetail(MethodData methodData,boolean exceptionFlag){
        if (methodData == null) {
            return null;
        }
        String oldMethodDetailId = methodData.getId();
        MethodDetailData oldMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, oldMethodDetailId), MethodDetailData.class);
        if (oldMethodDetailData == null) {
            oldMethodDetailData = new MethodDetailData();
            BeanUtils.copyProperties(methodData,oldMethodDetailData);
            oldMethodDetailData.setAvgExecuteTime(methodData.getRunTime());
            oldMethodDetailData.setMaxExecuteTime(methodData.getRunTime());
            oldMethodDetailData.setMinExecuteTime(methodData.getRunTime());
            oldMethodDetailData.setExceptionCount(exceptionFlag ? 1L : 0L);
        }else {
            BigDecimal newAvgExecuteTime = (methodData.getRunTime().add(oldMethodDetailData.getAvgExecuteTime())).divide(new BigDecimal("2"),2,RoundingMode.HALF_UP);
            oldMethodDetailData.setAvgExecuteTime(newAvgExecuteTime);
            BigDecimal newMaxExecuteTime = oldMethodDetailData.getMaxExecuteTime();
            if (methodData.getRunTime().compareTo(oldMethodDetailData.getMaxExecuteTime()) > 0) {
                newMaxExecuteTime = methodData.getRunTime();
            }
            BigDecimal newMinExecuteTime = oldMethodDetailData.getMinExecuteTime();
            if (methodData.getRunTime().compareTo(oldMethodDetailData.getMinExecuteTime()) < 0) {
                newMinExecuteTime = methodData.getRunTime();
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

    public void addControllerSortedSet(MethodData methodData,BigDecimal executeTime){
        if (methodData == null) {
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
}
