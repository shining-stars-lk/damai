package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Data
@ApiModel(value="RunJobDto", description ="RunJobDto")
public class RunJobDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="页码", required =true)
    @NotBlank
    private String id;
}
