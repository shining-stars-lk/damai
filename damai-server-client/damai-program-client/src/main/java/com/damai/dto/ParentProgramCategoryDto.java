package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 父节目类型 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ParentProgramCategoryDto", description ="父节目类型")
public class ParentProgramCategoryDto {
    
    @Schema(name ="parentProgramCategoryId", requiredMode= RequiredMode.REQUIRED, type ="Long", description ="父节目类型id")
    @NotNull
    private Long parentProgramCategoryId;
}
