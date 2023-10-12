package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="InfoDto", description ="信息数据查询")
public class InfoDto {
    
    @ApiModelProperty(name ="code", dataType ="String", value ="code码", required =true)
    @NotBlank
    private String code;
    
}