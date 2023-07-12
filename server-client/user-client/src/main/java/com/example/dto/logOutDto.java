package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="logOutDto", description ="用户")
public class logOutDto {
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户手机号", required =true)
    @NotBlank
    private String mobile;
}