package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: token dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="TokenDataDto", description ="token数据")
public class TokenDataDto {
    
    @Schema(name ="name", type ="String", description ="名字",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String name;
    
    @Schema(name ="secret", type ="String", description ="秘钥",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String secret;
}
