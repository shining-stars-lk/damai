package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 节目演出时间表
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
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
