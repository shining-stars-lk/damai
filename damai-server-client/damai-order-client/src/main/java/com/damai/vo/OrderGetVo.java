package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="OrderGetVo", description ="订单详情")
public class OrderGetVo {
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号")
    private Long orderNumber;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id")
    private Long programId;
    
    @ApiModelProperty(name ="programItemPicture", dataType ="String", value ="节目图片介绍")
    private String programItemPicture;

    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id")
    private Long userId;
    
    @ApiModelProperty(name ="programTitle", dataType ="String", value ="节目标题")
    private String programTitle;
    
    @ApiModelProperty(name ="programPlace", dataType ="String", value ="节目地点")
    private String programPlace;
    
    @ApiModelProperty(name ="programShowTime", dataType ="Date", value ="节目演出时间")
    private Date programShowTime;
    
    @ApiModelProperty(name ="programPermitChooseSeat", dataType ="Integer", value ="节目是否允许选座 1:允许选座 0:不允许选座")
    private Integer programPermitChooseSeat;
    
    @ApiModelProperty(name ="distributionMode", dataType ="String", value ="配送方式")
    private String distributionMode;
    
    @ApiModelProperty(name ="takeTicketMode", dataType ="String", value ="取票方式")
    private String takeTicketMode;

    @ApiModelProperty(name ="orderPrice", dataType ="BigDecimal", value ="订单价格")
    private BigDecimal orderPrice;
    
    @ApiModelProperty(name ="payOrderType", dataType ="Integer", value ="支付订单方式")
    private Integer payOrderType;
    
    @ApiModelProperty(name ="orderStatus", dataType ="Integer", value ="订单状态 1:未支付 2:已取消 3:已支付 4:已退单")
    private Integer orderStatus;
    
    @ApiModelProperty(name ="createOrderTime", dataType ="Date", value ="生成订单时间")
    private Date createOrderTime;
    
    @ApiModelProperty(name ="cancelOrderTime", dataType ="Date", value ="取消订单时间")
    private Date cancelOrderTime;
    
    @ApiModelProperty(name ="payOrderTime", dataType ="Date", value ="支付订单时间")
    private Date payOrderTime;
    
    @ApiModelProperty(name ="orderTicketInfoVoList", dataType ="List<OrderTicketInfoVo>", value ="购票订单集合")
    private List<OrderTicketInfoVo> orderTicketInfoVoList;
    
    @ApiModelProperty(name ="userAndTicketUserInfoVo", dataType ="UserAndTicketUserInfoVo", value ="用户和购票人信息")
    private UserAndTicketUserInfoVo userAndTicketUserInfoVo;
}
