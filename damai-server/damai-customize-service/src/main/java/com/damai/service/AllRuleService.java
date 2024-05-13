package com.damai.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.damai.util.StringUtil;
import com.damai.dto.AllRuleDto;
import com.damai.dto.DepthRuleDto;
import com.damai.enums.BaseCode;
import com.damai.enums.RuleStatus;
import com.damai.exception.DaMaiFrameException;
import com.damai.vo.AllDepthRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 所有规则 service
 * @author: 阿星不是程序员
 **/
@Service
public class AllRuleService {
    
    @Autowired
    private RuleService ruleService;
    
    @Autowired
    private DepthRuleService depthRuleService;
    
    @Transactional(rollbackFor = Exception.class)
    public void add(final AllRuleDto allRuleDto) {
        ruleService.add(allRuleDto.getRuleDto());
        depthRuleService.delAll();
        List<DepthRuleDto> depthRuleDtoList = allRuleDto.getDepthRuleDtoList();
        if (CollUtil.isNotEmpty(depthRuleDtoList)) {
            for (int i = 0; i < depthRuleDtoList.size(); i++) {
                DepthRuleDto depthRuleDto = depthRuleDtoList.get(i);
                checkTime(depthRuleDto.getStartTimeWindow(),depthRuleDto.getEndTimeWindow(),filterDepthRuleDtoList(depthRuleDtoList,i));
                depthRuleService.add(depthRuleDto);
            }
        }
        ruleService.saveAllRuleCache();
    }
    
    public void checkTime(String startTimeWindow, String endTimeWindow, List<DepthRuleDto> depthRuleDtoList){
        if (StringUtil.isEmpty(startTimeWindow) || StringUtil.isEmpty(endTimeWindow)) {
            return;
        }
        depthRuleDtoList = depthRuleDtoList.stream().filter(depthRuleDto -> {
            if (depthRuleDto.getStatus() != null) {
                if (depthRuleDto.getStatus().equals(RuleStatus.RUN.getCode())) {
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
                throw new DaMaiFrameException(BaseCode.API_RULE_TIME_WINDOW_INTERSECT);
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
