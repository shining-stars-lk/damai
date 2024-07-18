package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 主页节目列表查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramListDto", description ="主页节目列表")
public class ProgramListDto {
    
    @Schema(name ="areaId", type ="Long", description ="所在区域id")
    private Long areaId;
    
    @Schema(name ="parentProgramCategoryIds", type ="Long[]", description ="父节目类型id集合",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    @Size(max = 4)
    private List<Long> parentProgramCategoryIds;
}
