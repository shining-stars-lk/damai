package com.example.notice;

import cn.hutool.core.collection.CollectionUtil;
import com.example.core.RedisKeyEnum;
import com.example.notice.config.ApiStatNoticeProperties;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.structure.MethodDetailData;
import com.example.structure.MethodNoticeData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@Slf4j
@AllArgsConstructor
public class ApiStatNotice {
    
    private final ApiStatNoticeProperties apiStatNoticeProperties;
    
    private final RedisCache redisCache;
    
    private final List<Platform> platformList; 
    
    public void notice(){
        if (CollectionUtil.isEmpty(platformList)) {
            log.warn("platform empty");
            return;
        }
        List<MethodNoticeData> methodDetailDataList = getMethodDetailDataListStr();
        if (CollectionUtil.isEmpty(methodDetailDataList)) {
            log.warn("method detail data empty");
            return;
        }
        for (final Platform platform : platformList) {
            platform.sendMessage(methodDetailDataList);
        }
    }
    public List<MethodNoticeData> getMethodDetailDataListStr(){
        Set<TypedTuple<MethodDetailData>> set = redisCache.rangeByScoreWithScoreForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET), 
                apiStatNoticeProperties.getMin(), apiStatNoticeProperties.getMax(), apiStatNoticeProperties.getStart(), apiStatNoticeProperties.getEnd(), MethodDetailData.class);
        return set.stream().map(typedTuple -> {
            Double score = typedTuple.getScore();
            MethodDetailData value = typedTuple.getValue();
            MethodNoticeData methodNoticeData = new MethodNoticeData();
            methodNoticeData.setApi(value.getApi());
            methodNoticeData.setArgumentCount(value.getArgumentCount());
            methodNoticeData.setAvgExecuteTime(new BigDecimal(String.valueOf(score)));
            return methodNoticeData;
        }).collect(Collectors.toList());
    }
}
