package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramVo", description ="节目")
public class ProgramVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="id", type ="Long", description ="主键id")
    private Long id;
    
    @Schema(name ="programGroupId", type ="Long", description ="节目分组id")
    private Long programGroupId;
    
    @Schema(name ="prime", type ="Integer", description ="当属于同一个节目分组时 是否为主要节目 0:否 1:是")
    private Integer prime;
    
    @Schema(name ="programGroupVo", type ="ProgramGroupVo", description ="节目分组")
    private ProgramGroupVo programGroupVo;
    
    @Schema(name ="title", type ="Long", description ="标题")
    private String title;
    
    @Schema(name ="actor", type ="Long", description ="艺人")
    private String actor;
    
    @Schema(name ="place", type ="String", description ="地点")
    private String place;
    
    @Schema(name ="itemPicture", type ="String", description ="图片介绍")
    private String itemPicture;
    
    @Schema(name ="preSell", type ="Integer", description ="预售 1:是 0:否")
    private Integer preSell;
    
    @Schema(name ="preSellInstruction", type ="String", description ="预售说明")
    private String preSellInstruction;
    
    @Schema(name ="importantNotice", type ="String", description ="重要通知")
    private String importantNotice;
    
    @Schema(name ="areaId", type ="Long", description ="区域id")
    private Long areaId;

    @Schema(name ="areaName", type ="Long", description ="区域名字")
    private String areaName;
    
    @Schema(name ="programCategoryId", type ="Long", description ="节目类型表id")
    private Long programCategoryId;
    
    @Schema(name ="programCategoryName", type ="Long", description ="节目类型表名字")
    private String programCategoryName;
    
    @Schema(name ="parentProgramCategoryId", type ="Long", description ="父节目类型表id")
    private Long parentProgramCategoryId;
    
    @Schema(name ="parentProgramCategoryName", type ="String", description ="父节目类型名字")
    private String parentProgramCategoryName;
    
    @Schema(name ="detail", type ="String", description ="项目详情")
    private String detail;
    
    @Schema(name ="perOrderLimitPurchaseCount", type ="Integer", description ="每笔订单最多购买数量")
    private Integer perOrderLimitPurchaseCount;
    
    @Schema(name ="perAccountLimitPurchaseCount", type ="Integer", description ="每个账号最多购买数量")
    private Integer perAccountLimitPurchaseCount;
    
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
    
    @Schema(name ="permitRefund", type ="Integer", description ="是否允许退款  0:不支持退 1:条件退 2:全部退")
    private Integer permitRefund;
    
    @Schema(name ="refundExplain", type ="String", description ="退款说明")
    private String refundExplain;
    
    @Schema(name ="relNameTicketEntrance", type ="Integer", description ="实名制购票和入场 1:是 0:否")
    private Integer relNameTicketEntrance;
    
    @Schema(name ="relNameTicketEntranceExplain", type ="String", description ="实名制购票和入场说明")
    private String relNameTicketEntranceExplain;
    
    @Schema(name ="permitChooseSeat", type ="Integer", description ="是否允许选座 1:允许选座 0:不允许选座")
    private Integer permitChooseSeat;
    
    @Schema(name ="chooseSeatExplain", type ="String", description ="选座说明")
    private String chooseSeatExplain;

    @Schema(name ="electronicDeliveryTicket", type ="Integer", description ="电子票/快递票 0:都没有 1:电子票 2:快递票")
    private Integer electronicDeliveryTicket;
    
    @Schema(name ="electronicDeliveryTicketExplain", type ="String", description ="电子票说明")
    private String electronicDeliveryTicketExplain;
    
    @Schema(name ="electronicInvoice", type ="Integer", description ="电子发票 1:是 0:不是")
    private Integer electronicInvoice;
    
    @Schema(name ="electronicInvoiceExplain", type ="String", description ="电子发票说明")
    private String electronicInvoiceExplain;

    @Schema(name ="highHeat", type ="Integer", description ="高热度节目 0:否 1:是")
    private Integer highHeat;
    
    @Schema(name ="programStatus", type ="Integer", description ="节目状态 1:上架 0:下架")
    private Integer programStatus;
    
    @Schema(name ="issueTime", type ="Date", description ="上架发行时间")
    private Integer issueTime;
    
    /**
     * 业务字段
     * */
    @Schema(name ="showTime", type ="Date", description ="演出时间")
    private Date showTime;
    
    @Schema(name ="showDayTime", type ="Date", description ="演出时间(精确到天)")
    private Date showDayTime;
    
    @Schema(name ="showWeekTime", type ="String", description ="演出时间所在的星期")
    private String showWeekTime;
    
    @Schema(name ="ticketCategoryVoList", type ="List<TicketCategoryVo>", description ="节目票档")
    private List<TicketCategoryVo> ticketCategoryVoList;
}
