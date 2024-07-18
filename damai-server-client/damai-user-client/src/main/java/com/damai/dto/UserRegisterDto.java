package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户注册 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="UserRegisterDto", description ="注册用户")
public class UserRegisterDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    
    @Schema(name ="name", type ="String", description ="用户名字")
    private String name;

    @Schema(name ="relName", type ="String", description ="用户真实名字")
    private String relName;
    
    @Schema(name ="gender", type ="Integer", description ="性别 1:男 2:女")
    private Integer gender;
    
    @Schema(name ="password", type ="String", description ="密码",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String password;
    
    @Schema(name ="confirmPassword", type ="String", description ="二次确认的密码",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String confirmPassword;
    
    @Schema(name ="mobile", type ="String", description ="手机号",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String mobile;
    
    @Schema(name ="mailStatus", type ="Boolean", description ="是否邮箱认证 true:已验证 false:未验证")
    private Integer mailStatus;
    
    @Schema(name ="name", type ="String", description ="用户名字")
    private String mail;
    
    @Schema(name ="relAuthenticationStatus", type ="Boolean", description ="否实名认证 1:已验证 0:未验证")
    private Integer relAuthenticationStatus;
    
    @Schema(name ="idNumber", type ="String", description ="身份证号码")
    private String idNumber;
    
    @Schema(name ="id", type ="captchaId", description ="captchaId 调用是否需要校验验证码接口返回",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String captchaId;
    
    @Schema(name ="captchaVerification", type ="String", description ="二次校验验证码 " +
            "前端将 校验验证码返回的 token---pointJson 拼接后 使用AES加密 加密的秘钥使用获取验证码返回的secretKey")
    private String captchaVerification;
    
}
