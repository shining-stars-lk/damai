package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.example.core.RedisKeyEnum;
import com.example.dto.RuleDto;
import com.example.dto.RuleGetDto;
import com.example.dto.RuleStatusDto;
import com.example.dto.RuleUpdateDto;
import com.example.entity.Rule;
import com.example.enums.RuleStatus;
import com.example.mapper.RuleMapper;
import com.example.redis.RedisKeyWrap;
import com.example.redis.RedisCache;
import com.example.util.DateUtils;
import com.example.vo.RuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-30
 **/
@Service
public class RuleService {

    @Autowired
    private RuleMapper ruleMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Resource
    private UidGenerator uidGenerator;
    
    @Transactional
    public void ruleAdd(RuleDto ruleDto) {
        saveCache(add(ruleDto));
    }
    @Transactional
    public String add(RuleDto ruleDto) {
        delAll();
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleDto,rule);
        rule.setId(String.valueOf(uidGenerator.getUID()));
        rule.setCreateTime(DateUtils.now());
        ruleMapper.insert(rule);
        return rule.getId();
    }
    
    @Transactional
    public void ruleUpdate(final RuleUpdateDto ruleUpdateDto) {
        update(ruleUpdateDto);
        saveCache(ruleUpdateDto.getId());
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
        saveCache(ruleStatusDto.getId());
    }
    
    @Transactional
    public void updateStatus(final RuleStatusDto ruleStatusDto) {
        Rule rule = new Rule();
        rule.setId(ruleStatusDto.getId());
        rule.setStatus(ruleStatusDto.getStatus());
        ruleMapper.updateById(rule);
        saveCache(rule.getId());
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
    
    
    public void saveCache(String id){
        Optional.ofNullable(ruleMapper.selectById(id)).ifPresent(rule -> {
            if (rule.getStatus() == RuleStatus.RUN.getCode()) {
                redisCache.set(RedisKeyWrap.createRedisKey(RedisKeyEnum.RULE),rule);
            }else {
                redisCache.del(RedisKeyWrap.createRedisKey(RedisKeyEnum.RULE));
            }
        });
    }
}
