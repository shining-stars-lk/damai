package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 座位相关信息 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="SeatRelateInfoVo", description ="座位相关信息")
public class SeatRelateInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id")
    private Long programId;
    
    @ApiModelProperty(name ="place", dataType ="String", value ="地点")
    private String place;
    
    @ApiModelProperty(name ="showTime", dataType ="Date", value ="演出时间")
    private Date showTime;
    
    @ApiModelProperty(name ="showWeekTime", dataType ="String", value ="演出时间所在的星期")
    private String showWeekTime;
    
    @ApiModelProperty(name ="priceList", dataType ="List<String>", value ="价格集合")
    private List<String> priceList;
    
    @ApiModelProperty(name ="seatVoMap", dataType ="Map<String,List<SeatVo>>", value ="座位集合")
    private Map<String,List<SeatVo>> seatVoMap;
   
}
