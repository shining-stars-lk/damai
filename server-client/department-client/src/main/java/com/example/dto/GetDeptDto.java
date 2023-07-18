package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-05
 **/
@Data
@ApiModel(value="GetDeptDto", description ="GetDeptDto")
public class GetDeptDto {
    
    @ApiModelProperty(name ="typeCode", dataType ="String", value ="类型编码", required =true)
    @NotBlank
    private String typeCode;
}
