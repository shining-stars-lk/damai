package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="GetChannelDataByCodeDto", description ="渠道数据查询")
public class GetChannelDataByCodeDto {
    
    @ApiModelProperty(name ="code", dataType ="String", value ="code码", required =true)
    @NotBlank
    private String code;
    
}