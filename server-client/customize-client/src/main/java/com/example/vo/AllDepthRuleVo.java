package com.example.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-11
 **/
@Data
public class AllDepthRuleVo {

    private RuleVo ruleVo;
    
    private List<DepthRuleVo> depthRuleVoList;
}
