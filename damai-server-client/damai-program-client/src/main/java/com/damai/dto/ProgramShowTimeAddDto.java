package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目演出时间添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramShowTimeAddDto", description ="节目演出时间添加")
public class ProgramShowTimeAddDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id",required = true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="showTime", dataType ="Date", value ="演出时间",required = true)
    @NotNull
    private Date showTime;
    
    @ApiModelProperty(name ="showDayTime", dataType ="Date", value ="演出时间(精确到天)",required = true)
    @NotNull
    private Date showDayTime;
    
    @ApiModelProperty(name ="showWeekTime", dataType ="String", value ="演出时间所在的星期",required = true)
    @NotBlank
    private String showWeekTime;
}
