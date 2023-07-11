package com.example.service;

import cn.hutool.core.collection.CollUtil;
import com.example.dto.AllRuleDto;
import com.example.vo.AllDepthRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: toolkit
 * @description:
 * @author: k
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
        if (CollUtil.isNotEmpty(allRuleDto.getDepthRuleDtoList())) {
            allRuleDto.getDepthRuleDtoList().forEach(depthRuleDto -> depthRuleService.add(depthRuleDto));
        }
        depthRuleService.saveCache();
    }
    
    public AllDepthRuleVo get() {
        AllDepthRuleVo allDepthRuleVo = new AllDepthRuleVo();
        allDepthRuleVo.setRuleVo(ruleService.get());
        allDepthRuleVo.setDepthRuleVoList(depthRuleService.selectList());
        return allDepthRuleVo;
    }
}
