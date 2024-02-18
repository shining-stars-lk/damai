package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 所有规则 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="AllDepthRuleVo", description ="全部规则")
public class AllDepthRuleVo {
    
    @ApiModelProperty(name ="ruleDto", dataType ="RuleDto", value ="普通规则")
    private RuleVo ruleVo;
    
    @ApiModelProperty(name ="depthRuleDtoList", dataType ="DepthRuleDto[]", value ="深度规则")
    private List<DepthRuleVo> depthRuleVoList;
}
