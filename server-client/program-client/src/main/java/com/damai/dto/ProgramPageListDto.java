package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目分页查询 dto
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramPageListDto", description ="节目分页")
public class ProgramPageListDto extends BasePageDto{
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="所在区域id")
    private Long areaId;
    
    @ApiModelProperty(name ="programCategoryIds", dataType ="Long[]", value ="节目类型id集合")
    private List<Long> programCategoryIds;
    
    @ApiModelProperty(name ="parentProgramCategoryIds", dataType ="Long[]", value ="父节目类型id集合")
    private List<Long> parentProgramCategoryIds;
    
    @ApiModelProperty(name ="showDayTime", dataType ="Date", value ="今天/明天/按日历")
    private Date showDayTime;
    
    @ApiModelProperty(name ="timeType", dataType ="int", value ="1:本周内 2:一个月内")
    private Integer timeType;
    
    /**
     * 业务字段，后端自己填充
     * */
    private Date time;
}
