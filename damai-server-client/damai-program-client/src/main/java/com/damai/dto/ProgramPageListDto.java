package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
    
    @ApiModelProperty(name ="parentProgramCategoryId", dataType ="Long", value ="父节目类型id")
    private Long parentProgramCategoryId;
    
    @ApiModelProperty(name ="programCategoryId", dataType ="Long", value ="节目类型id")
    private Long programCategoryId;
    
    @ApiModelProperty(name ="timeType", dataType ="Integer", value ="0:全部 1:今天 2:明天 3:一周内 4:一个月内 5:按日历")
    @NotNull
    private Integer timeType;
    
    @ApiModelProperty(name ="startDateTime", dataType ="Date", value ="开始时间(如果timeType = 5，此项必填)")
    private Date startDateTime;
    
    @ApiModelProperty(name ="endDateTime", dataType ="Date", value ="结束时间(如果timeType = 5，此项必填)")
    private Date endDateTime;
}
