package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目简单信息 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramSimpleInfoVo", description ="节目简单信息")
public class ProgramSimpleInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="主键id")
    private Long programId;
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="地区id")
    private Long areaId;
    
    @ApiModelProperty(name ="areaIdName", dataType ="String", value ="地区名字")
    private String areaIdName;
}

