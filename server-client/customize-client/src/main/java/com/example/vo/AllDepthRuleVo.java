package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-11
 **/
@Data
@ApiModel(value="AllDepthRuleVo", description ="全部规则")
public class AllDepthRuleVo {
    
    @ApiModelProperty(name ="ruleDto", dataType ="RuleDto", value ="普通规则")
    private RuleVo ruleVo;
    
    @ApiModelProperty(name ="depthRuleDtoList", dataType ="DepthRuleDto[]", value ="深度规则")
    private List<DepthRuleVo> depthRuleVoList;
}
