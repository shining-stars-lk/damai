package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@Data
@ApiModel(value="UserRegisterDto", description ="注册用户")
public class UserRegisterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户名字")
    private String name;

    @ApiModelProperty(name ="relName", dataType ="String", value ="用户真实名字")
    private String relName;
    
    @ApiModelProperty(name ="gender", dataType ="Integer", value ="性别 1:男 2:女")
    private Integer gender;
    
    @ApiModelProperty(name ="password", dataType ="String", value ="密码",required = true)
    private String password;
    
    @ApiModelProperty(name ="mobile", dataType ="String", value ="手机号",required = true)
    @NotBlank
    private String mobile;
    
    @ApiModelProperty(name ="mailStatus", dataType ="Boolean", value ="是否邮箱认证 true:已验证 false:未验证")
    private Integer mailStatus;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户名字")
    private String mail;
    
    @ApiModelProperty(name ="relAuthenticationStatus", dataType ="Boolean", value ="否实名认证 1:已验证 0:未验证")
    private Integer relAuthenticationStatus;
    
    @ApiModelProperty(name ="idNumber", dataType ="String", value ="身份证号码")
    private String idNumber;
    
    
    
}
