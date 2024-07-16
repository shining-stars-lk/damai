package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户实名认证 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="UserAuthenticationDto", description ="用户实名认证")
public class UserAuthenticationDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="用户id",required = true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="relName", dataType ="String", value ="用户真实名字",required = true)
    @NotBlank
    private String relName;
    
    @ApiModelProperty(name ="idNumber", dataType ="String", value ="身份证号码",required = true)
    @NotBlank
    private String idNumber;
    
}
