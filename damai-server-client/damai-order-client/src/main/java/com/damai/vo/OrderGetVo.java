package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单详情 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="OrderGetVo", description ="订单详情")
public class OrderGetVo {
    
    @Schema(name ="orderNumber", type ="Long", description ="订单编号")
    private Long orderNumber;
    
    @Schema(name ="programId", type ="Long", description ="节目表id")
    private Long programId;
    
    @Schema(name ="programItemPicture", type ="String", description ="节目图片介绍")
    private String programItemPicture;

    @Schema(name ="userId", type ="Long", description ="用户id")
    private Long userId;
    
    @Schema(name ="programTitle", type ="String", description ="节目标题")
    private String programTitle;
    
    @Schema(name ="programPlace", type ="String", description ="节目地点")
    private String programPlace;
    
    @Schema(name ="programShowTime", type ="Date", description ="节目演出时间")
    private Date programShowTime;
    
    @Schema(name ="programPermitChooseSeat", type ="Integer", description ="节目是否允许选座 1:允许选座 0:不允许选座")
    private Integer programPermitChooseSeat;
    
    @Schema(name ="distributionMode", type ="String", description ="配送方式")
    private String distributionMode;
    
    @Schema(name ="takeTicketMode", type ="String", description ="取票方式")
    private String takeTicketMode;

    @Schema(name ="orderPrice", type ="BigDecimal", description ="订单价格")
    private BigDecimal orderPrice;
    
    @Schema(name ="payOrderType", type ="Integer", description ="支付订单方式")
    private Integer payOrderType;
    
    @Schema(name ="orderStatus", type ="Integer", description ="订单状态 1:未支付 2:已取消 3:已支付 4:已退单")
    private Integer orderStatus;
    
    @Schema(name ="createOrderTime", type ="Date", description ="生成订单时间")
    private Date createOrderTime;
    
    @Schema(name ="cancelOrderTime", type ="Date", description ="取消订单时间")
    private Date cancelOrderTime;
    
    @Schema(name ="payOrderTime", type ="Date", description ="支付订单时间")
    private Date payOrderTime;
    
    @Schema(name ="orderTicketInfoVoList", type ="List<OrderTicketInfoVo>", description ="购票订单集合")
    private List<OrderTicketInfoVo> orderTicketInfoVoList;
    
    @Schema(name ="userAndTicketUserInfoVo", type ="UserAndTicketUserInfoVo", description ="用户和购票人信息")
    private UserAndTicketUserInfoVo userAndTicketUserInfoVo;
}
