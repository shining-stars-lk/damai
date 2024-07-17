package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 座位相关信息 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="SeatRelateInfoVo", description ="座位相关信息")
public class SeatRelateInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    
    @Schema(name ="programId", type ="Long", description ="节目表id")
    private Long programId;
    
    @Schema(name ="place", type ="String", description ="地点")
    private String place;
    
    @Schema(name ="showTime", type ="Date", description ="演出时间")
    private Date showTime;
    
    @Schema(name ="showWeekTime", type ="String", description ="演出时间所在的星期")
    private String showWeekTime;
    
    @Schema(name ="priceList", type ="List<String>", description ="价格集合")
    private List<String> priceList;
    
    @Schema(name ="seatVoMap", type ="Map<String,List<SeatVo>>", description ="座位集合")
    private Map<String,List<SeatVo>> seatVoMap;
   
}
