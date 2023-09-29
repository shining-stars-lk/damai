package com.example.service;

import com.example.core.RedisKeyEnum;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.structure.MethodDetailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
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
}
