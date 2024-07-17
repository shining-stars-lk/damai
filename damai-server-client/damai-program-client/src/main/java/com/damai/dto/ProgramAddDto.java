package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramAddDto", description ="节目添加")
public class ProgramAddDto {
    
    @Schema(name ="areaId", type ="Long", description ="所在区域id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long areaId;
    
    @Schema(name ="programCategoryId", type ="Long", description ="节目类型表id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long programCategoryId;
    
    @Schema(name ="parentProgramCategoryId", type ="Long", description ="父节目类型表id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long parentProgramCategoryId;
    
    @Schema(name ="title", type ="Long", description ="标题", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private String title;
    
    @Schema(name ="actor", type ="String", description ="艺人")
    private String actor;
    
    @Schema(name ="place", type ="String", description ="地点")
    private String place;
    
    @Schema(name ="itemPicture", type ="String", description ="项目图片")
    private String itemPicture;
    
    @Schema(name ="detail", type ="String", description ="项目详情")
    private String detail;
    
    @Schema(name ="purchaseLimitRule", type ="String", description ="限购规则")
    private String purchaseLimitRule;
    
    @Schema(name ="refundTicketRule", type ="String", description ="退票/换票规则")
    private String refundTicketRule;
    
    @Schema(name ="deliveryInstruction", type ="String", description ="配送信息说明")
    private String deliveryInstruction;
    
    @Schema(name ="entryRule", type ="String", description ="入场规则")
    private String entryRule;
    
    @Schema(name ="childPurchase", type ="String", description ="儿童购票")
    private String childPurchase;
    
    @Schema(name ="invoiceSpecification", type ="String", description ="发票说明")
    private String invoiceSpecification;
    
    @Schema(name ="realTicketPurchaseRule", type ="String", description ="实名购票规则")
    private String realTicketPurchaseRule;
    
    @Schema(name ="abnormalOrderDescription", type ="String", description ="异常排单说明")
    private String abnormalOrderDescription;
    
    @Schema(name ="kindReminder", type ="String", description ="温馨提示")
    private String kindReminder;
    
    @Schema(name ="performanceDuration", type ="String", description ="演出时长")
    private String performanceDuration;
    
    @Schema(name ="entryTime", type ="String", description ="入场时间")
    private String entryTime;
    
    @Schema(name ="minPerformanceCount", type ="Integer", description ="最低演出曲目")
    private Integer minPerformanceCount;
    
    @Schema(name ="mainActor", type ="String", description ="主要演员")
    private String mainActor;
    
    @Schema(name ="minPerformanceDuration", type ="String", description ="最低演出时长")
    private String minPerformanceDuration;
    
    @Schema(name ="prohibitedItem", type ="String", description ="禁止携带物品")
    private String prohibitedItem;
    
    @Schema(name ="depositSpecification", type ="String", description ="寄存说明")
    private String depositSpecification;
    
    @Schema(name ="totalCount", type ="Long", description ="大麦网初始开售时全场可售门票总张数")
    private Long totalCount;
    
    @Schema(name ="permitRefund", type ="Integer", description ="是否允许退款 1:允许 0:不允许")
    private Integer permitRefund;
    
    @Schema(name ="permitChooseSeat", type ="Integer", description ="是否允许选座 1:允许选座 0:不允许选座")
    private Integer permitChooseSeat;
    
    @Schema(name ="electronicDeliveryTicket", type ="Integer", description ="电子票/快递票 1:是 0:不是")
    private Integer electronicDeliveryTicket;
    
    @Schema(name ="electronicInvoice", type ="Integer", description ="电子发票 1:是 0:不是")
    private Integer electronicInvoice;
}
