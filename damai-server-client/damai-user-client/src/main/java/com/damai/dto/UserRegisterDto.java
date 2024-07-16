package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户注册 dto
 * @author: 阿星不是程序员
 **/
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
    @NotBlank
    private String password;
    
    @ApiModelProperty(name ="confirmPassword", dataType ="String", value ="二次确认的密码",required = true)
    @NotBlank
    private String confirmPassword;
    
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
    
    @ApiModelProperty(name ="id", dataType ="captchaId", value ="captchaId 调用是否需要校验验证码接口返回",required = true)
    @NotBlank
    private String captchaId;
    
    @ApiModelProperty(name ="captchaVerification", dataType ="String", value ="二次校验验证码 " +
            "前端将 校验验证码返回的 token---pointJson 拼接后 使用AES加密 加密的秘钥使用获取验证码返回的secretKey")
    private String captchaVerification;
    
}
