package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 手机手机号 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="UserExistDto", description ="用户是否存在")
public class UserExistDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="mobile", dataType ="String", value ="手机号",required = true)
    @NotBlank
    private String mobile;
    
}
