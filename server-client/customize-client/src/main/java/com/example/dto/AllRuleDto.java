package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-11
 **/
@Data
public class AllRuleDto {

    @NotNull
    private RuleDto ruleDto;

    private List<DepthRuleDto> depthRuleDtoList;
}
