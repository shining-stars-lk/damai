package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="TestDto", description ="测试")
public class TestDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    @NotBlank
    private String id;
    
    @ApiModelProperty(name ="sleepTime", dataType ="Integer", value ="执行时间")
    private Integer sleepTime;
}
