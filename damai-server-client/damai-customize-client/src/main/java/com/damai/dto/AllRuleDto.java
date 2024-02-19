package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 所有规则 dto
 * @author: 阿宽不是程序员
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
