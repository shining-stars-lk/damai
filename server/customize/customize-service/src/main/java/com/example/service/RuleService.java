package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.example.core.CacheKeyEnum;
import com.example.dto.RuleDto;
import com.example.dto.RuleGetDto;
import com.example.dto.RuleStatusDto;
import com.example.dto.RuleUpdateDto;
import com.example.entity.Rule;
import com.example.enums.RuleStatus;
import com.example.mapper.RuleMapper;
import com.example.redis.CacheKeyWrap;
import com.example.redis.DistributCache;
import com.example.util.DateUtils;
import com.example.vo.RuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Service
public class RuleService {

    @Autowired
    private RuleMapper ruleMapper;
    
    @Autowired
    private DistributCache distributCache;
    
    @Resource
    private UidGenerator uidGenerator;
    
    public void add(RuleDto ruleDto) {
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleDto,rule);
        rule.setId(String.valueOf(uidGenerator.getUID()));
        rule.setCreateTime(DateUtils.now());
        ruleMapper.insert(rule);
        saveCache(rule.getId());
    }
    
    public void update(final RuleUpdateDto ruleUpdateDto) {
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleUpdateDto,rule);
        ruleMapper.updateById(rule);
    }
    
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
    
    
    public void saveCache(String id){
        Optional.ofNullable(ruleMapper.selectById(id)).ifPresent(rule -> {
            if (rule.getStatus() == RuleStatus.RUN.getCode()) {
                distributCache.set(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.RULE),rule);
            }else {
                distributCache.del(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.RULE));
            }
        });
    }
    
    
}
