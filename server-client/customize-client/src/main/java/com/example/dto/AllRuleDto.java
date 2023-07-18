package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-11
 **/
@Data
@ApiModel(value="AllRuleDto", description ="全部规则")
public class AllRuleDto {
    
    @ApiModelProperty(name ="ruleDto", dataType ="RuleDto", value ="普通规则", required =true)
    @NotNull
    private RuleDto ruleDto;
    
    @ApiModelProperty(name ="depthRuleDtoList", dataType ="DepthRuleDto[]", value ="深度规则")
    private List<DepthRuleDto> depthRuleDtoList;
}
