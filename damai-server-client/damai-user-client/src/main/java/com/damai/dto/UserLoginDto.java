package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户登录 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="UserLoginDto", description ="用户登录")
public class UserLoginDto {
    
    @ApiModelProperty(name ="code", dataType ="String", value ="渠道code 0001:pc网站", required = true)
    @NotBlank
    private String code;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户手机号")
    private String mobile;
    
    @ApiModelProperty(name ="email", dataType ="String", value ="用户邮箱")
    private String email;
    
    @ApiModelProperty(name ="password", dataType ="String", value ="密码", required = true)
    @NotBlank
    private String password;
}