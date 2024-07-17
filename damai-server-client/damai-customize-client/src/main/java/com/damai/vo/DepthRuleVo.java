package com.damai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 深度规则 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="DepthRuleVo", description ="深度规则")
public class DepthRuleVo {
    
    @Schema(name ="id", type ="String", description ="深度规则id", requiredMode= RequiredMode.REQUIRED)
    private Long id;
    
    @Schema(name ="startTimeWindow", type ="String", description ="开始时间窗口", requiredMode= RequiredMode.REQUIRED)
    private String startTimeWindow;
    
    @Schema(name ="endTimeWindow", type ="String", description ="结束时间窗口", requiredMode= RequiredMode.REQUIRED)
    private String endTimeWindow;
    
    @Schema(name ="statTime", type ="Integer", description ="统计时间", requiredMode= RequiredMode.REQUIRED)
    private Integer statTime;
    
    @Schema(name ="statTimeType", type ="Integer", description ="统计时间类型 1:秒 2:分钟", requiredMode= RequiredMode.REQUIRED)
    private Integer statTimeType;
    
    @Schema(name ="threshold", type ="Integer", description ="阈值", requiredMode= RequiredMode.REQUIRED)
    private Integer threshold;
    
    @Schema(name ="effectiveTime", type ="Integer", description ="规则生效限制时间", requiredMode= RequiredMode.REQUIRED)
    private Integer effectiveTime;
    
    @Schema(name ="effectiveTimeType", type ="Integer", description ="规则生效限制时间类型 1:秒 2:分钟", requiredMode= RequiredMode.REQUIRED)

    private Integer effectiveTimeType;
    
    private String limitApi;
    
    @Schema(name ="message", type ="String", description ="提示信息")
    private String message;
    
    @Schema(name ="status", type ="Integer", description ="状态 1生效 0禁用", requiredMode= RequiredMode.REQUIRED)
    private Integer status;
    
    @Schema(name ="createTime", type ="Date", description ="创建时间", requiredMode= RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
