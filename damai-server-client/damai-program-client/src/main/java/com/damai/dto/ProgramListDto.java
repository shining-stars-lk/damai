package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 主页节目列表查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramListDto", description ="主页节目列表")
public class ProgramListDto {
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="所在区域id")
    private Long areaId;
    
    @ApiModelProperty(name ="parentProgramCategoryIds", dataType ="Long[]", value ="父节目类型id集合",required = true)
    @NotNull
    @Size(max = 4)
    private List<Long> parentProgramCategoryIds;
}
