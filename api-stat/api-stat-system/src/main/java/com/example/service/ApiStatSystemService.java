package com.example.service;

import com.example.core.RedisKeyEnum;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiStatSystemService {

    @Autowired
    private RedisCache redisCache;

    public Set<MethodDetailData> getControllerMethods() {
        Set<ZSetOperations.TypedTuple<MethodDetailData>> typedTuples = redisCache.reverseRangeWithScoreForZSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_SORTED_SET), 0, -1, MethodDetailData.class);
        Set<MethodDetailData> set = typedTuples.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toSet());
        return set;
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
        List<MethodDetailData> list = typedTuples.stream().map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toList());
        PageVo<MethodDetailData> pageVo = new PageVo<>();
        pageVo.setPageTotal(pageTotal);
        pageVo.setPageNo(pageNo);
        pageVo.setPageSize(pageSize);
        pageVo.setData(list);
        return pageVo;
    }

    public MethodDetailData getMethodChainList(String controllerMethod) {
//        MethodData controllerMethodData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_METHOD_DATA, controllerMethod), MethodData.class);
//        MethodHierarchy controllerMethodHierarchy = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_HIERARCHY, controllerMethod), MethodHierarchy.class);
//        if (controllerMethodData == null || controllerMethodHierarchy == null) {
//            return controllerMethodDetailData;
//        }
        MethodDetailData controllerMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, controllerMethod), MethodDetailData.class);
        if (controllerMethodDetailData == null) {
            return null;
        }
        Set<String> serviceMethodNameSet = redisCache.membersForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_CONTROLLER_CHILDREN_SET, controllerMethod), String.class);

        List<MethodDetailData> serviceList = new ArrayList<>();
        for (String serviceMethodName : serviceMethodNameSet) {
            //MethodData serviceMethodData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_METHOD_DATA, serviceMethodName), MethodData.class);
            //MethodHierarchy serviceMethodHierarchy = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_HIERARCHY, serviceMethodName), MethodHierarchy.class);
//            if (serviceMethodData == null || serviceMethodHierarchy == null) {
//                continue;
//            }
            MethodDetailData serviceMethodDetailData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_DETAIL, serviceMethodName), MethodDetailData.class);
            if (serviceMethodDetailData == null) {
                continue;
            }
            //            MethodDetailData serviceMethodDetailData = new MethodDetailData();
//            BeanUtils.copyProperties(serviceMethodData,serviceMethodDetailData);
//            BeanUtils.copyProperties(serviceMethodHierarchy,serviceMethodDetailData);

            Set<String> daoMethodNameSet = redisCache.membersForSet(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_SERVICE_CHILDREN_SET, serviceMethodName), String.class);

            List<MethodDetailData> daoList = new ArrayList<>();
            for (String daoMethodName : daoMethodNameSet) {
//                MethodData daoMethodData = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_DAO_METHOD_DATA, daoMethodName), MethodData.class);
//                MethodHierarchy daoMethodHierarchy = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.API_STAT_METHOD_HIERARCHY, daoMethodName), MethodHierarchy.class);
//
//                if (daoMethodData == null || daoMethodHierarchy == null) {
//                    continue;
//                }
//                MethodDetailData daoMethodDetailData = new MethodDetailData();
//                BeanUtils.copyProperties(daoMethodData,daoMethodDetailData);
//                BeanUtils.copyProperties(daoMethodHierarchy,daoMethodDetailData);
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