package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramAddDto", description ="节目添加")
public class ProgramAddDto {
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="所在区域id", required = true)
    @NotNull
    private Long areaId;
    
    @ApiModelProperty(name ="programCategoryId", dataType ="Long", value ="节目类型表id", required = true)
    @NotNull
    private Long programCategoryId;
    
    @ApiModelProperty(name ="parentProgramCategoryId", dataType ="Long", value ="父节目类型表id", required = true)
    @NotNull
    private Long parentProgramCategoryId;
    
    @ApiModelProperty(name ="title", dataType ="Long", value ="标题", required = true)
    @NotNull
    private String title;
    
    @ApiModelProperty(name ="actor", dataType ="String", value ="艺人")
    private String actor;
    
    @ApiModelProperty(name ="place", dataType ="String", value ="地点")
    private String place;
    
    @ApiModelProperty(name ="itemPicture", dataType ="String", value ="项目图片")
    private String itemPicture;
    
    @ApiModelProperty(name ="detail", dataType ="String", value ="项目详情")
    private String detail;
    
    @ApiModelProperty(name ="purchaseLimitRule", dataType ="String", value ="限购规则")
    private String purchaseLimitRule;
    
    @ApiModelProperty(name ="refundTicketRule", dataType ="String", value ="退票/换票规则")
    private String refundTicketRule;
    
    @ApiModelProperty(name ="deliveryInstruction", dataType ="String", value ="配送信息说明")
    private String deliveryInstruction;
    
    @ApiModelProperty(name ="entryRule", dataType ="String", value ="入场规则")
    private String entryRule;
    
    @ApiModelProperty(name ="childPurchase", dataType ="String", value ="儿童购票")
    private String childPurchase;
    
    @ApiModelProperty(name ="invoiceSpecification", dataType ="String", value ="发票说明")
    private String invoiceSpecification;
    
    @ApiModelProperty(name ="realTicketPurchaseRule", dataType ="String", value ="实名购票规则")
    private String realTicketPurchaseRule;
    
    @ApiModelProperty(name ="abnormalOrderDescription", dataType ="String", value ="异常排单说明")
    private String abnormalOrderDescription;
    
    @ApiModelProperty(name ="kindReminder", dataType ="String", value ="温馨提示")
    private String kindReminder;
    
    @ApiModelProperty(name ="performanceDuration", dataType ="String", value ="演出时长")
    private String performanceDuration;
    
    @ApiModelProperty(name ="entryTime", dataType ="String", value ="入场时间")
    private String entryTime;
    
    @ApiModelProperty(name ="minPerformanceCount", dataType ="Integer", value ="最低演出曲目")
    private Integer minPerformanceCount;
    
    @ApiModelProperty(name ="mainActor", dataType ="String", value ="主要演员")
    private String mainActor;
    
    @ApiModelProperty(name ="minPerformanceDuration", dataType ="String", value ="最低演出时长")
    private String minPerformanceDuration;
    
    @ApiModelProperty(name ="prohibitedItem", dataType ="String", value ="禁止携带物品")
    private String prohibitedItem;
    
    @ApiModelProperty(name ="depositSpecification", dataType ="String", value ="寄存说明")
    private String depositSpecification;
    
    @ApiModelProperty(name ="totalCount", dataType ="Long", value ="大麦网初始开售时全场可售门票总张数")
    private Long totalCount;
    
    @ApiModelProperty(name ="permitRefund", dataType ="Integer", value ="是否允许退款 1:允许 0:不允许")
    private Integer permitRefund;
    
    @ApiModelProperty(name ="permitChooseSeat", dataType ="Integer", value ="是否允许选座 1:允许选座 0:不允许选座")
    private Integer permitChooseSeat;
    
    @ApiModelProperty(name ="electronicDeliveryTicket", dataType ="Integer", value ="电子票/快递票 1:是 0:不是")
    private Integer electronicDeliveryTicket;
    
    @ApiModelProperty(name ="electronicInvoice", dataType ="Integer", value ="电子发票 1:是 0:不是")
    private Integer electronicInvoice;
}
