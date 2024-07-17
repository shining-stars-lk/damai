package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户退出登录 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="UserLogoutDto", description ="用户退出登录")
public class UserLogoutDto {
    
    @Schema(name ="code", type ="String", description ="渠道code 0001:pc网站", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String code;
    
    @Schema(name ="id", type ="Long", description ="token", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String token;
}