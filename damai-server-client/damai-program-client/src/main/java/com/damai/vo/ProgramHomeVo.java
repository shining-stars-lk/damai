package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 主页节目列表 Vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramListVo", description ="节目主页列表")
public class ProgramHomeVo implements Serializable  {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="categoryName", dataType ="String", value ="类型名字")
    private String categoryName;
    
    @ApiModelProperty(name ="programListVoList", dataType ="array", value ="节目列表")
    private List<ProgramListVo> programListVoList;
}
