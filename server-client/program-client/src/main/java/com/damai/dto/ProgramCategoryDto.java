package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-05
 **/
@Data
@ApiModel(value="ProgramCategoryDto", description ="节目类型")
public class ProgramCategoryDto {
    
    @ApiModelProperty(name ="type", dataType ="Integer", value ="1:一级种类 2:二级种类", required =true)
    @NotNull
    private Integer type;
}
