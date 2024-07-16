package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户密码更新 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="UserUpdatePasswordDto", description ="修改用户密码")
public class UserUpdatePasswordDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="用户id",required = true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="password", dataType ="String", value ="密码",required = true)
    @NotBlank
    private String password;
    
}
