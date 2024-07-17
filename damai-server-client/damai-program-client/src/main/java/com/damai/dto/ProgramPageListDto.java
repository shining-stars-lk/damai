package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目分页查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramPageListDto", description ="节目分页")
public class ProgramPageListDto extends BasePageDto{
    
    @Schema(name ="areaId", type ="Long", description ="所在区域id")
    private Long areaId;
    
    @Schema(name ="parentProgramCategoryId", type ="Long", description ="父节目类型id")
    private Long parentProgramCategoryId;
    
    @Schema(name ="programCategoryId", type ="Long", description ="节目类型id")
    private Long programCategoryId;
    
    @Schema(name ="timeType", type ="Integer", description ="0:全部 1:今天 2:明天 3:一周内 4:一个月内 5:按日历",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer timeType;
    
    @Schema(name ="startDateTime", type ="Date", description ="开始时间(如果timeType = 5，此项必填)")
    private Date startDateTime;
    
    @Schema(name ="endDateTime", type ="Date", description ="结束时间(如果timeType = 5，此项必填)")
    private Date endDateTime;
    
    @Schema(name ="type", type ="Integer", description ="查询方式 1:相关度排序(默认) 2:推荐排序 3:最近开场 4:最新上架")
    private Integer type = 1;
}
