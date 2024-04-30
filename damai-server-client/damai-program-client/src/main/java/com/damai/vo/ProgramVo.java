package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramVo", description ="节目")
public class ProgramVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="主键id")
    private Long id;
    
    @ApiModelProperty(name ="programGroupId", dataType ="Long", value ="节目分组id")
    private Long programGroupId;
    
    @ApiModelProperty(name ="programGroupVo", dataType ="ProgramGroupVo", value ="节目分组")
    private ProgramGroupVo programGroupVo;
    
    @ApiModelProperty(name ="title", dataType ="Long", value ="标题")
    private String title;
    
    @ApiModelProperty(name ="actor", dataType ="Long", value ="艺人")
    private String actor;
    
    @ApiModelProperty(name ="place", dataType ="String", value ="地点")
    private String place;
    
    @ApiModelProperty(name ="itemPicture", dataType ="String", value ="图片介绍")
    private String itemPicture;
    
    @ApiModelProperty(name ="preSell", dataType ="Integer", value ="预售 1:是 0:否")
    private Integer preSell;
    
    @ApiModelProperty(name ="preSellInstruction", dataType ="String", value ="预售说明")
    private String preSellInstruction;
    
    @ApiModelProperty(name ="importantNotice", dataType ="String", value ="重要通知")
    private String importantNotice;
    
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
    
    @ApiModelProperty(name ="parentProgramCategoryName", dataType ="String", value ="父节目类型名字")
    private String parentProgramCategoryName;
    
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
    
    /**
     * 是否允许退款 1:允许 0:不允许
     */
    @ApiModelProperty(name ="permitRefund", dataType ="Integer", value ="否允许退款 1:允许 0:不允许")
    private Integer permitRefund;
    
    /**
     * 是否允许选座 1:允许选座 0:不允许选座
     */
    @ApiModelProperty(name ="permitChooseSeat", dataType ="Integer", value ="是否允许选座 1:允许选座 0:不允许选座")
    private Integer permitChooseSeat;
    
    /**
     * 电子票/快递票 1:是 0:不是
     */
    @ApiModelProperty(name ="electronicDeliveryTicket", dataType ="Integer", value ="电子票/快递票 1:是 0:不是")
    private Integer electronicDeliveryTicket;
    
    /**
     * 电子发票 1:是 0:不是
     */
    @ApiModelProperty(name ="electronicInvoice", dataType ="Integer", value ="电子发票 1:是 0:不是")
    private Integer electronicInvoice;
    
    /**
     * 高热度节目 0:否 1:是
     * */
    @ApiModelProperty(name ="highHeat", dataType ="Integer", value ="高热度节目 0:否 1:是")
    private Integer highHeat;
    
    @ApiModelProperty(name ="programStatus", dataType ="Integer", value ="节目状态 1:上架 0:下架")
    private Integer programStatus;
    
    /**
     * 业务字段
     * */
    @ApiModelProperty(name ="showTime", dataType ="Date", value ="演出时间")
    private Date showTime;
    
    @ApiModelProperty(name ="showDayTime", dataType ="Date", value ="演出时间(精确到天)")
    private Date showDayTime;
    
    @ApiModelProperty(name ="showWeekTime", dataType ="String", value ="演出时间所在的星期")
    private String showWeekTime;
    
    @ApiModelProperty(name ="ticketCategoryVoList", dataType ="List<TicketCategoryVo>", value ="节目票档")
    private List<TicketCategoryVo> ticketCategoryVoList;
    
    @ApiModelProperty(name ="seatVoList", dataType ="List<SeatVo>", value ="座位列表")
    private List<SeatVo> seatVoList;
}
