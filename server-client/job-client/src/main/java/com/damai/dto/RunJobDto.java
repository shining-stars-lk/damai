package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Data
@ApiModel(value="RunJobDto", description ="RunJobDto")
public class RunJobDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="页码", required =true)
    @NotNull
    private Long id;
}
