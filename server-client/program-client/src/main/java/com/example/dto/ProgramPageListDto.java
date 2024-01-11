package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-05
 **/
@Data
@ApiModel(value="ProgramPageListDto", description ="节目")
public class ProgramPageListDto extends BasePageDto{
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="所在区域id")
    private Long areaId;
    
    @ApiModelProperty(name ="programCategoryIds", dataType ="Long[]", value ="节目类型id集合")
    private List<Long> programCategoryIds;
    
    @ApiModelProperty(name ="parentProgramCategoryIds", dataType ="Long[]", value ="父节目类型id集合")
    private List<Long> parentProgramCategoryIds;
    
    @ApiModelProperty(name ="showTime", dataType ="Date", value ="今天/明天/按日历")
    private Date showTime;
    
    @ApiModelProperty(name ="timeType", dataType ="int", value ="1:本周内 2:一个月内")
    private int timeType;
    
    /**
     * 业务字段，后端自己填充
     * */
    private Date time;
}
