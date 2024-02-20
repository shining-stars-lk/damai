package com.damai.service.init;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.damai.BusinessThreadPool;
import com.damai.core.RedisKeyEnum;
import com.damai.entity.ProgramCategory;
import com.damai.init.InitData;
import com.damai.mapper.ProgramCategoryMapper;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
import com.damai.servicelock.LockType;
import com.damai.servicelock.annotion.ServiceLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.damai.core.DistributedLockConstants.PROGRAM_CATEGORY_LOCK;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目种类缓存
 * @author: 阿宽不是程序员
 **/
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
    
    @Override
    public int executeOrder() {
        return 1;
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
