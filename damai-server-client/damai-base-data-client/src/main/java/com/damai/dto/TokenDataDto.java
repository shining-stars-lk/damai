package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: token dto
 * @author: 阿宽不是程序员
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
