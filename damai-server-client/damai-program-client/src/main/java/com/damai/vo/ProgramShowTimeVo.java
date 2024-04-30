package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目演出时间 实体
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramShowTimeVo", description ="节目演出时间")
public class ProgramShowTimeVo {
    

    /**
     * 主键id
     */
    @ApiModelProperty(name ="id", dataType ="Long", value ="主键id")
    private Long id;

    /**
     * 节目表id
     */
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id")
    private Long programId;

    /**
     * 演出时间
     */
    @ApiModelProperty(name ="showTime", dataType ="Date", value ="演出时间")
    private Date showTime;
    
    /**
     * 演出时间(精确到天)
     */
    @ApiModelProperty(name ="showDayTime", dataType ="Date", value ="演出时间(精确到天)")
    private Date showDayTime;

    /**
     * 演出时间所在的星期
     */
    @ApiModelProperty(name ="showWeekTime", dataType ="String", value ="演出时间所在的星期")
    private String showWeekTime;
}
