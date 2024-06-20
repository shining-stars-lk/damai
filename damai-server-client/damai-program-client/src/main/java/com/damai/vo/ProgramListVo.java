package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目列表 vo
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramListVo", description ="节目列表")
public class ProgramListVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="主键id")
    private Long id;
    
    @ApiModelProperty(name ="title", dataType ="Long", value ="标题")
    private String title;
    
    @ApiModelProperty(name ="actor", dataType ="Long", value ="艺人")
    private String actor;
    
    @ApiModelProperty(name ="place", dataType ="String", value ="地点")
    private String place;
    
    @ApiModelProperty(name ="itemPicture", dataType ="itemPicture", value ="图片介绍")
    private String itemPicture;
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="区域id")
    private Long areaId;

    @ApiModelProperty(name ="areaName", dataType ="Long", value ="区域名字")
    private String areaName;
    
    @ApiModelProperty(name ="programCategoryId", dataType ="Long", value ="节目类型表id")
    private Long programCategoryId;
    
    @ApiModelProperty(name ="programCategoryName", dataType ="Long", value ="节目类型表名字")
    private String programCategoryName;
    
    @ApiModelProperty(name ="parentProgramCategoryId", dataType ="Long", value ="父节目类型表id")
    private Long parentProgramCategoryId;
    
    @ApiModelProperty(name ="parentProgramCategoryName", dataType ="Long", value ="父节目类型表名字")
    private String parentProgramCategoryName;
    
    @ApiModelProperty(name ="showTime", dataType ="Date", value ="演出时间")
    private Date showTime;
    
    @ApiModelProperty(name ="showDayTime", dataType ="Date", value ="演出时间(精确到天)")
    private Date showDayTime;
    
    @ApiModelProperty(name ="showWeekTime", dataType ="String", value ="演出时间所在的星期")
    private String showWeekTime;
    
    @ApiModelProperty(name ="minPrice", dataType ="BigDecimal", value ="最低价格")
    private BigDecimal minPrice;
    
    @ApiModelProperty(name ="maxPrice", dataType ="BigDecimal", value ="最高价格")
    private BigDecimal maxPrice;
}
