package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.example.core.RedisKeyEnum;
import com.example.dto.DepthRuleDto;
import com.example.dto.DepthRuleStatusDto;
import com.example.dto.DepthRuleUpdateDto;
import com.example.entity.DepthRule;
import com.example.enums.RuleStatus;
import com.example.mapper.DepthRuleMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.util.DateUtils;
import com.example.vo.DepthRuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Service
public class DepthRuleService {

    @Autowired
    private DepthRuleMapper depthRuleMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Resource
    private UidGenerator uidGenerator;
    
    @Transactional
    public void add(DepthRuleDto depthRuleDto) {
        DepthRule depthRule = new DepthRule();
        BeanUtils.copyProperties(depthRuleDto,depthRule);
        depthRule.setId(String.valueOf(uidGenerator.getUID()));
        depthRule.setCreateTime(DateUtils.now());
        depthRuleMapper.insert(depthRule);
        saveCache();
    }
    
    @Transactional
    public void update(final DepthRuleUpdateDto depthRuleUpdateDto) {
        DepthRule depthRule = new DepthRule();
        BeanUtils.copyProperties(depthRuleUpdateDto,depthRule);
        depthRuleMapper.updateById(depthRule);
    }
    
    @Transactional
    public void updateStatus(final DepthRuleStatusDto depthRuleStatusDto) {
        DepthRule depthRule = new DepthRule();
        depthRule.setId(depthRuleStatusDto.getId());
        depthRule.setStatus(depthRuleStatusDto.getStatus());
        depthRuleMapper.updateById(depthRule);
        saveCache();
    }
    
    public List<DepthRuleVo> selectList() {
        List<DepthRule> depthRules = depthRuleMapper.selectList(null);
        List<DepthRuleVo> depthRuleVos = depthRules.stream().map(depthRule -> {
            DepthRuleVo depthRuleVo = new DepthRuleVo();
            BeanUtils.copyProperties(depthRule, depthRuleVo);
            return depthRuleVo;
        }).collect(Collectors.toList());
        return depthRuleVos;
    }
    
    
    public void saveCache(){
        List<DepthRule> depthRules = depthRuleMapper.selectList(null);
        depthRules = depthRules.stream().filter(depthRule -> depthRule.getStatus() == RuleStatus.RUN.getCode()).collect(Collectors.toList());
        if (depthRules.size() > 0) {
            redisCache.set(RedisKeyWrap.cacheKeyBuild(RedisKeyEnum.RULE),depthRules);
        }else {
            redisCache.del(RedisKeyWrap.cacheKeyBuild(RedisKeyEnum.RULE));
        }
    }
}
