package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户邮箱更新 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="UserUpdateEmailDto", description ="修改用户邮箱")
public class UserUpdateEmailDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="用户id",required = true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="email", dataType ="String", value ="邮箱",required = true)
    @NotBlank
    private String email;
    
}
