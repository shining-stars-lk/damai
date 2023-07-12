package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-05
 **/
@Data
@ApiModel(value="TokenDataDto", description ="token数据")
public class TokenDataDto {
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名字", required =true)
    @NotBlank
    private String name;
    
    @ApiModelProperty(name ="secret", dataType ="String", value ="秘钥", required =true)
    @NotBlank
    private String secret;
}
