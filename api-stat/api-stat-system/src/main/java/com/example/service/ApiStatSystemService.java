package com.example.service;

import com.example.core.RedisKeyEnum;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.structure.MethodData;
import com.example.structure.MethodDetailData;
import com.example.structure.MethodHierarchy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiStatSystemService {

    @Autowired
    private RedisCache redisCache;

    public Set<MethodDetailData> getControllerMethods() {
        Set<ZSetOperations.TypedTuple<MethodDetailData>> typedTuples = redisCache.rangeWithScoreForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET), 1, 10, MethodDetailData.class);
        Set<MethodDetailData> set = typedTuples.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toSet());
        return set;
    }

    public List getMethodChainList(String controllerMethod) {
        MethodData controllerMethodData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_METHOD_DATA, controllerMethod), MethodData.class);
        MethodHierarchy controllerMethodHierarchy = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_HIERARCHY, controllerMethod), MethodHierarchy.class);
        Set<String> serviceMethodNameSet = redisCache.membersForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_CHILDREN_SET, controllerMethod), String.class);
        for (String serviceMethodName : serviceMethodNameSet) {
            Set<String> daoMethodNameSet = redisCache.membersForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_CHILDREN_SET, serviceMethodName), String.class);
            for (String daoMethodName : daoMethodNameSet) {

            }
        }
        return null;
    }
}
