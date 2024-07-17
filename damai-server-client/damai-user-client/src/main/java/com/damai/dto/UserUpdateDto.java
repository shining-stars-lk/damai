package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户更新 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="UserUpdateDto", description ="修改用户")
public class UserUpdateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="id", type ="Long", description ="用户id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long id;
    
    @Schema(name ="name", type ="String", description ="用户名字")
    private String name;

    @Schema(name ="relName", type ="String", description ="用户真实名字")
    private String relName;
    
    @Schema(name ="gender", type ="Integer", description ="性别 1:男 2:女")
    private Integer gender;
    
    @Schema(name ="mobile", type ="String", description ="手机号")
    private String mobile;
    
    @Schema(name ="idNumber", type ="String", description ="身份证号码")
    private String idNumber;
    
}
