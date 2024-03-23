package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目搜索 dto
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramSearchDto", description ="节目搜索")
public class ProgramSearchDto extends ProgramPageListDto{
    
    @ApiModelProperty(name ="content", dataType ="String", value ="搜索内容")
    private String content;
}
