package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 普通规则 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="RuleDto", description ="普通规则")
public class RuleDto {
    
    @Schema(name ="statTime", type ="Integer", description ="统计时间", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer statTime;
    
    @Schema(name ="statTimeType", type ="Integer", description ="统计时间类型 1:秒 2:分钟", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer statTimeType;
   
    @Schema(name ="threshold", type ="Integer", description ="阈值", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer threshold;
    
    @Schema(name ="effectiveTime", type ="Integer", description ="规则生效限制时间", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer effectiveTime;
    
    @Schema(name ="effectiveTimeType", type ="Integer", description ="规则生效限制时间类型 1:秒 2:分钟", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer effectiveTimeType;
    
    private String limitApi;
    
    @Schema(name ="message", type ="String", description ="提示信息")
    private String message;
}
