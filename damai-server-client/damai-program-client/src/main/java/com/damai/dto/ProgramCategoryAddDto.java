package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目类型添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramCategoryAddDto", description ="节目类型")
public class ProgramCategoryAddDto implements Serializable {

    private static final long serialVersionUID = 1L;
    

    /**
     * 父区域id
     */
    @ApiModelProperty(name ="parentId", dataType ="Long", value ="父区域id",required = true)
    @NotNull
    private Long parentId;

    /**
     * 区域名字
     */
    @ApiModelProperty(name ="name", dataType ="String", value ="区域名字",required = true)
    @NotNull
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    @ApiModelProperty(name ="type", dataType ="Integer", value ="1:一级种类 2:二级种类",required = true)
    @NotNull
    private Integer type;
}
