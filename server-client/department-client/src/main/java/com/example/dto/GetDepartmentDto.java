package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Data
@ApiModel(value="GetDepartmentDto", description ="部门")
public class GetDepartmentDto {
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    @NotBlank
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名字")
    private String name;
    
    @ApiModelProperty(name ="sleepTime", dataType ="Long", value ="执行时间")
    private Long sleepTime;
}
