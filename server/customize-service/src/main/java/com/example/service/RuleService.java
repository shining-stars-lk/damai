package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.core.RedisKeyEnum;
import com.example.dto.RuleDto;
import com.example.dto.RuleGetDto;
import com.example.dto.RuleStatusDto;
import com.example.dto.RuleUpdateDto;
import com.example.entity.DepthRule;
import com.example.entity.Rule;
import com.example.enums.RuleStatus;
import com.example.mapper.DepthRuleMapper;
import com.example.mapper.RuleMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.vo.RuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Service
public class RuleService {

    @Autowired
    private RuleMapper ruleMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private DepthRuleMapper depthRuleMapper;
    
    @Resource
    private UidGenerator uidGenerator;
    
    @Transactional
    public void ruleAdd(RuleDto ruleDto) {
        add(ruleDto);
        saveAllRuleCache();
    }
    @Transactional
    public String add(RuleDto ruleDto) {
        delAll();
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleDto,rule);
        rule.setId(String.valueOf(uidGenerator.getUID()));
        rule.setCreateTime(DateUtil.date());
        ruleMapper.insert(rule);
        return rule.getId();
    }
    
    @Transactional
    public void ruleUpdate(final RuleUpdateDto ruleUpdateDto) {
        update(ruleUpdateDto);
        saveAllRuleCache();
    }
    
    @Transactional
    public void update(final RuleUpdateDto ruleUpdateDto) {
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleUpdateDto,rule);
        ruleMapper.updateById(rule);
    }
    
    @Transactional
    public void ruleUpdateStatus(final RuleStatusDto ruleStatusDto) {
        updateStatus(ruleStatusDto);
        saveAllRuleCache();
    }
    
    @Transactional
    public void updateStatus(final RuleStatusDto ruleStatusDto) {
        Rule rule = new Rule();
        rule.setId(ruleStatusDto.getId());
        rule.setStatus(ruleStatusDto.getStatus());
        ruleMapper.updateById(rule);
        saveAllRuleCache();
    }
    
    public RuleVo get(final RuleGetDto ruleGetDto) {
        RuleVo ruleVo = new RuleVo();
        Optional.ofNullable(ruleMapper.selectById(ruleGetDto.getId())).ifPresent(rule -> {
            BeanUtils.copyProperties(rule,ruleVo);
        });
        return ruleVo;
    }
    
    public RuleVo get() {
        RuleVo ruleVo = new RuleVo();
        Optional.ofNullable(ruleMapper.selectOne(null)).ifPresent(rule -> {
            BeanUtils.copyProperties(rule,ruleVo);
        });
        return ruleVo;
    }
    
    public void delAll(){
        ruleMapper.delAll();
    }
    
    
    public void saveAllRuleCache(){
        Map<String, Object> map = new HashMap<>(2);
        
        LambdaQueryWrapper<Rule> ruleQueryWrapper = Wrappers.lambdaQuery(Rule.class).eq(Rule::getStatus,RuleStatus.RUN.getCode());
        Rule rule = ruleMapper.selectOne(ruleQueryWrapper);
        if (Optional.ofNullable(rule).isPresent()) {
            map.put(RedisKeyWrap.createRedisKey(RedisKeyEnum.RULE).getRelKey(),rule);
        }
        LambdaQueryWrapper<DepthRule> depthRuleQueryWrapper = Wrappers.lambdaQuery(DepthRule.class).eq(DepthRule::getStatus,RuleStatus.RUN.getCode());
        List<DepthRule> depthRules = depthRuleMapper.selectList(depthRuleQueryWrapper);
        if (CollUtil.isNotEmpty(depthRules)) {
            map.put(RedisKeyWrap.createRedisKey(RedisKeyEnum.DEPTH_RULE).getRelKey(),depthRules);
        }
        redisCache.del(RedisKeyWrap.createRedisKey(RedisKeyEnum.ALL_RULE_HASH));
        if (map.size() > 0 && Objects.nonNull(map.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.RULE).getRelKey()))) {
            redisCache.putHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.ALL_RULE_HASH),map);
        }
    }
}
