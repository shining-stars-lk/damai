package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.core.StringUtil;
import com.example.dto.DepthRuleDto;
import com.example.dto.DepthRuleStatusDto;
import com.example.dto.DepthRuleUpdateDto;
import com.example.entity.DepthRule;
import com.example.enums.BaseCode;
import com.example.enums.RuleStatus;
import com.example.exception.ToolkitException;
import com.example.mapper.DepthRuleMapper;
import com.example.redis.RedisCache;
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
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Service
public class DepthRuleService {

    @Autowired
    private DepthRuleMapper depthRuleMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private RuleService ruleService;
    @Resource
    private UidGenerator uidGenerator;
    
    @Transactional
    public void depthRuleAdd(DepthRuleDto depthRuleDto) {
        check(depthRuleDto.getStartTimeWindow(),depthRuleDto.getEndTimeWindow());
        add(depthRuleDto);
        ruleService.saveAllRuleCache();
    }
    
    public void check(String startTimeWindow, String endTimeWindow){
        if (StringUtil.isEmpty(startTimeWindow) || StringUtil.isEmpty(endTimeWindow)) {
            return;
        }
        LambdaQueryWrapper<DepthRule> queryWrapper = Wrappers.lambdaQuery(DepthRule.class).eq(DepthRule::getStatus, RuleStatus.RUN.getCode());
        List<DepthRule> depthRules = depthRuleMapper.selectList(queryWrapper);
        for (final DepthRule depthRule : depthRules) {
            long checkStartTimeWindowTimestamp = getTimeWindowTimestamp(startTimeWindow);
            long checkEndTimeWindowTimestamp = getTimeWindowTimestamp(endTimeWindow);
            long startTimeWindowTimestamp = getTimeWindowTimestamp(depthRule.getStartTimeWindow());
            long endTimeWindowTimestamp = getTimeWindowTimestamp(depthRule.getEndTimeWindow());
            boolean checkStartLimitTimeResult = checkStartTimeWindowTimestamp >= startTimeWindowTimestamp && checkStartTimeWindowTimestamp <= endTimeWindowTimestamp;
            boolean checkEndLimitTimeResult = checkEndTimeWindowTimestamp >= startTimeWindowTimestamp && checkEndTimeWindowTimestamp <= endTimeWindowTimestamp;
            if (checkStartLimitTimeResult || checkEndLimitTimeResult) {
                throw new ToolkitException(BaseCode.API_RULE_TIME_WINDOW_INTERSECT);
            }
        }
    }
    
    public long getTimeWindowTimestamp(String timeWindow){
        String today = DateUtil.today();
        return DateUtil.parse(today + " " + timeWindow).getTime();
    }
    
    @Transactional
    public void add(DepthRuleDto depthRuleDto) {
        DepthRule depthRule = new DepthRule();
        BeanUtils.copyProperties(depthRuleDto,depthRule);
        depthRule.setId(String.valueOf(uidGenerator.getUID()));
        depthRule.setCreateTime(DateUtils.now());
        depthRuleMapper.insert(depthRule);
    }
    
    @Transactional
    public void depthRuleUpdate(final DepthRuleUpdateDto depthRuleUpdateDto) {
        update(depthRuleUpdateDto);
        ruleService.saveAllRuleCache();
    }
    
    @Transactional
    public void update(final DepthRuleUpdateDto depthRuleUpdateDto) {
        DepthRule depthRule = new DepthRule();
        BeanUtils.copyProperties(depthRuleUpdateDto,depthRule);
        depthRuleMapper.updateById(depthRule);
    }
    
    @Transactional
    public void depthRuleUpdateStatus(final DepthRuleStatusDto depthRuleStatusDto) {
        updateStatus(depthRuleStatusDto);
        ruleService.saveAllRuleCache();
    }
    @Transactional
    public void updateStatus(final DepthRuleStatusDto depthRuleStatusDto) {
        DepthRule depthRule = new DepthRule();
        depthRule.setId(depthRuleStatusDto.getId());
        depthRule.setStatus(depthRuleStatusDto.getStatus());
        depthRuleMapper.updateById(depthRule);
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
    
    @Transactional
    public void delAll(){
        depthRuleMapper.delAll();
    }
}
