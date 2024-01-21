package com.example.service.init;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.BusinessThreadPool;
import com.example.core.RedisKeyEnum;
import com.example.entity.ProgramCategory;
import com.example.init.InitData;
import com.example.mapper.ProgramCategoryMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.redisson.LockType;
import com.example.servicelock.annotion.ServiceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.core.DistributedLockConstants.PROGRAM_CATEGORY_LOCK;

@Component
public class ProgramCategoryInitData implements InitData {
    
    @Autowired
    private ProgramCategoryMapper programCategoryMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramCategoryInitData programCategoryInitDataProxy;
    
    
    @Override
    public void init() {
        BusinessThreadPool.execute(() -> {
            programCategoryInitDataProxy.programCategoryRedisDataInit();
        });
    }
    
    @ServiceLock(lockType= LockType.Write,name = PROGRAM_CATEGORY_LOCK,keys = {"#all"})
    public void programCategoryRedisDataInit(){
        QueryWrapper<ProgramCategory> lambdaQueryWrapper = Wrappers.emptyWrapper();
        List<ProgramCategory> programCategoryList = programCategoryMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(programCategoryList)) {
            Map<String, ProgramCategory> programCategoryMap = programCategoryList.stream().collect(
                    Collectors.toMap(p -> String.valueOf(p.getId()), p -> p, (v1, v2) -> v2));
            redisCache.putHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_CATEGORY_HASH),programCategoryMap);
        }
    }
}
