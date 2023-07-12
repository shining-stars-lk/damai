package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Data
@ApiModel(value="GetEmployeeDto", description ="职员")
public class GetEmployeeDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    @NotBlank
    private String id;
    
    @ApiModelProperty(name ="sleepTime", dataType ="Long", value ="执行时间")
    private Long sleepTime;
}
