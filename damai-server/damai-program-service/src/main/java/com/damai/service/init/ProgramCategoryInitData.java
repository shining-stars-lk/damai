package com.damai.service.init;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.damai.BusinessThreadPool;
import com.damai.core.RedisKeyManage;
import com.damai.entity.ProgramCategory;
import com.damai.initialize.base.AbstractApplicationPostConstructHandler;
import com.damai.mapper.ProgramCategoryMapper;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.servicelock.LockType;
import com.damai.servicelock.annotion.ServiceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.damai.core.DistributedLockConstants.PROGRAM_CATEGORY_LOCK;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目种类缓存
 * @author: 阿星不是程序员
 **/
@Component
public class ProgramCategoryInitData extends AbstractApplicationPostConstructHandler {
    
    @Autowired
    private ProgramCategoryMapper programCategoryMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramCategoryInitData programCategoryInitDataProxy;
    
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
    
    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
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
            redisCache.putHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_CATEGORY_HASH),programCategoryMap);
        }
    }
}
