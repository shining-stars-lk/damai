package com.example.service;

import com.example.core.RedisKeyEnum;
import com.example.dto.MethodChainDto;
import com.example.dto.PageDto;
import com.example.info.PageInfo;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.structure.MethodDetailData;
import com.example.util.PageUtil;
import com.example.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiStatSystemService {

    @Autowired
    private RedisCache redisCache;

    public List<MethodDetailData> getControllerMethods() {
        Set<ZSetOperations.TypedTuple<MethodDetailData>> typedTuples = redisCache.reverseRangeWithScoreForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET), 0, -1, MethodDetailData.class);
        List<MethodDetailData> list = typedTuples.stream().map(typedTuple -> {
            Double score = typedTuple.getScore();
            MethodDetailData methodDetailData = typedTuple.getValue();
            methodDetailData.setExecuteTime(new BigDecimal(String.valueOf(score)));
            methodDetailData.setAvgExecuteTime(new BigDecimal(String.valueOf(score)));
            return methodDetailData;
        }).collect(Collectors.toList());
        return list;
    }

    public PageVo<MethodDetailData> getControllerMethodsPage(PageDto pageDto){
        PageInfo pageInfo = PageUtil.getPageInfo(pageDto, () -> redisCache.sizeForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET)));
        Long pageNo = pageInfo.getPageNo();
        Long pageSize = pageInfo.getPageSize();
        Long pageTotal = pageInfo.getPageTotal();

        // 计算起始索引和结束索引
        long start = pageInfo.getStart();
        long end = pageInfo.getEnd();
        Set<ZSetOperations.TypedTuple<MethodDetailData>> typedTuples = redisCache.reverseRangeWithScoreForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET), start, end, MethodDetailData.class);
        List<MethodDetailData> list = typedTuples.stream().map(typedTuple -> {
            Double score = typedTuple.getScore();
            MethodDetailData methodDetailData = typedTuple.getValue();
            methodDetailData.setExecuteTime(new BigDecimal(String.valueOf(score)));
            methodDetailData.setAvgExecuteTime(new BigDecimal(String.valueOf(score)));
            return methodDetailData;
        }).collect(Collectors.toList());
        PageVo<MethodDetailData> pageVo = new PageVo<>();
        pageVo.setPageTotal(pageTotal);
        pageVo.setPageNo(pageNo);
        pageVo.setPageSize(pageSize);
        pageVo.setData(list);
        return pageVo;
    }

    public MethodDetailData getMethodChainList(MethodChainDto methodChainDto) {
        String methodDetailDataId = methodChainDto.getMethodDetailDataId();
        MethodDetailData controllerMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, methodDetailDataId), MethodDetailData.class);
        if (controllerMethodDetailData == null) {
            return null;
        }
        Set<String> serviceMethodNameSet = redisCache.membersForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_CHILDREN_SET, methodDetailDataId), String.class);

        List<MethodDetailData> serviceList = new ArrayList<>();
        for (String serviceMethodName : serviceMethodNameSet) {
            MethodDetailData serviceMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, serviceMethodName), MethodDetailData.class);
            if (serviceMethodDetailData == null) {
                continue;
            }
            Set<String> daoMethodNameSet = redisCache.membersForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_CHILDREN_SET, serviceMethodName), String.class);

            List<MethodDetailData> daoList = new ArrayList<>();
            for (String daoMethodName : daoMethodNameSet) {
                MethodDetailData daoMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, daoMethodName), MethodDetailData.class);
                daoList.add(daoMethodDetailData);
            }
            serviceMethodDetailData.setChildrenMethodList(daoList);

            serviceList.add(serviceMethodDetailData);
        }
        controllerMethodDetailData.setChildrenMethodList(serviceList);
        return controllerMethodDetailData;
    }
}
