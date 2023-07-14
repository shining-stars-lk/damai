package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.example.core.StringUtil;
import com.example.dto.AllRuleDto;
import com.example.dto.DepthRuleDto;
import com.example.enums.BaseCode;
import com.example.enums.RuleStatus;
import com.example.exception.ToolkitException;
import com.example.vo.AllDepthRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-07-11
 **/
@Service
public class AllRuleService {
    
    @Autowired
    private RuleService ruleService;
    
    @Autowired
    private DepthRuleService depthRuleService;
    
    @Transactional
    public void add(final AllRuleDto allRuleDto) {
        ruleService.delAll();
        String ruleId = ruleService.add(allRuleDto.getRuleDto());
        ruleService.saveCache(ruleId);
        depthRuleService.delAll();
        List<DepthRuleDto> depthRuleDtoList = allRuleDto.getDepthRuleDtoList();
        if (CollUtil.isNotEmpty(depthRuleDtoList)) {
            for (int i = 0; i < depthRuleDtoList.size(); i++) {
                DepthRuleDto depthRuleDto = depthRuleDtoList.get(i);
                checkTime(depthRuleDto.getStartTimeWindow(),depthRuleDto.getEndTimeWindow(),filterDepthRuleDtoList(depthRuleDtoList,i));
                depthRuleService.add(depthRuleDto);
            }
        }
        depthRuleService.saveCache();
    }
    
    public void checkTime(String startTimeWindow, String endTimeWindow, List<DepthRuleDto> depthRuleDtoList){
        if (StringUtil.isEmpty(startTimeWindow) || StringUtil.isEmpty(endTimeWindow)) {
            return;
        }
        depthRuleDtoList = depthRuleDtoList.stream().filter(depthRuleDto -> {
            if (depthRuleDto.getStatus() != null) {
                if (depthRuleDto.getStatus() == RuleStatus.RUN.getCode()) {
                    return true;
                }else {
                    return false;
                }
            }else {
                return true;
            }
        }).collect(Collectors.toList());
        for (final DepthRuleDto depthRuleDto : depthRuleDtoList) {
            long checkStartTimeWindowTimestamp = getTimeWindowTimestamp(startTimeWindow);
            long checkEndTimeWindowTimestamp = getTimeWindowTimestamp(endTimeWindow);
            long startTimeWindowTimestamp = getTimeWindowTimestamp(depthRuleDto.getStartTimeWindow());
            long endTimeWindowTimestamp = getTimeWindowTimestamp(depthRuleDto.getEndTimeWindow());
            boolean checkStartLimitTimeResult = checkStartTimeWindowTimestamp >= startTimeWindowTimestamp && checkStartTimeWindowTimestamp <= endTimeWindowTimestamp;
            boolean checkEndLimitTimeResult = checkEndTimeWindowTimestamp >= startTimeWindowTimestamp && checkEndTimeWindowTimestamp <= endTimeWindowTimestamp;
            if (checkStartLimitTimeResult || checkEndLimitTimeResult) {
                throw new ToolkitException(BaseCode.API_RULE_TIME_WINDOW_INTERSECT);
            }
        }
    }
    
    public List<DepthRuleDto> filterDepthRuleDtoList(List<DepthRuleDto> depthRuleDtoList, int coord){
        List<DepthRuleDto> fiterDepthRuleDtoList = new ArrayList<>();
        for (int i = 0; i < depthRuleDtoList.size(); i++) {
            if (i != coord) {
                fiterDepthRuleDtoList.add(depthRuleDtoList.get(i));
            }
        }
        return fiterDepthRuleDtoList;
    }
    public long getTimeWindowTimestamp(String timeWindow){
        String today = DateUtil.today();
        return DateUtil.parse(today + " " + timeWindow).getTime();
    }
    
    public AllDepthRuleVo get() {
        AllDepthRuleVo allDepthRuleVo = new AllDepthRuleVo();
        allDepthRuleVo.setRuleVo(ruleService.get());
        allDepthRuleVo.setDepthRuleVoList(depthRuleService.selectList());
        return allDepthRuleVo;
    }
}
