package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseTableData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_program")
public class Program extends BaseTableData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 节目分组id
     */
    private Long programGroupId;
    
    /**
     * 当属于同一个节目分组时 是否为主要节目 0:否 1:是
     */
    private Integer prime;

    /**
     * 所在区域id
     */
    private Long areaId;

    /**
     * 节目类型表id
     */
    private Long programCategoryId;
    
    /**
     * 父节目类型表id
     * */
    private Long parentProgramCategoryId;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 艺人
     * */
    private String actor;
    
    /**
     * 地点
     * */
    private String place;
    
    /**
     * 项目图片
     * */
    private String itemPicture;
    
    /**
     * 预售 1:是 0:否
     * */
    private Integer preSell;
    
    /**
     * 预售说明
     * */
    private String preSellInstruction;
    
    /**
     * 重要通知
     * */
    private String importantNotice;

    /**
     * 项目详情
     */
    private String detail;

    /**
     * 每笔订单最多购买数量
     * */
    private Integer perOrderLimitPurchaseCount;
    
    /**
     * 每个账号最多购买数量
     * */
    private Integer perAccountLimitPurchaseCount;

    /**
     * 退票/换票规则
     */
    private String refundTicketRule;

    /**
     * 配送信息说明
     */
    private String deliveryInstruction;

    /**
     * 入场规则
     */
    private String entryRule;

    /**
     * 儿童购票
     */
    private String childPurchase;

    /**
     * 发票说明
     */
    private String invoiceSpecification;

    /**
     * 实名购票规则
     */
    private String realTicketPurchaseRule;

    /**
     * 异常排单说明
     */
    private String abnormalOrderDescription;

    /**
     * 温馨提示
     */
    private String kindReminder;

    /**
     * 演出时长
     */
    private String performanceDuration;

    /**
     * 入场时间
     */
    private String entryTime;
    
    /**
     * 最低演出曲目
     * */
    private Integer minPerformanceCount;
    
    /**
     * 主要演员
     * */
    private String mainActor;
    
    /**
     * 最低演出时长
     * */
    private String minPerformanceDuration;

    /**
     * 禁止携带物品
     */
    private String prohibitedItem;

    /**
     * 寄存说明
     */
    private String depositSpecification;

    /**
     * 大麦网初始开售时全场可售门票总张数
     */
    private Long totalCount;
    
    /**
     * 是否允许退款  0:不支持退 1:条件退 2:全部退
     */
    private Integer permitRefund;
    
    /**
     * 退款说明
     * */
    private String refundExplain;
    
    /**
     * 实名制购票和入场 1:是 0:否
     * */
    private Integer relNameTicketEntrance;
    
    /**
     * 实名制购票和入场说明
     * */
    private String relNameTicketEntranceExplain;
    
    /**
     * 是否允许选座 1:允许选座 0:不允许选座
     */
    private Integer permitChooseSeat;
    
    /**
     * 选座说明
     * */
    private String chooseSeatExplain;
    
    /**
     * 电子票/快递票 0:都没有1:电子票 2:快递票
     */
    private Integer electronicDeliveryTicket;
    
    /**
     * 电子票说明
     */
    private String electronicDeliveryTicketExplain;
    
    /**
     * 电子发票 1:是 0:不是
     */
    private Integer electronicInvoice;
    
    /**
     * 电子发票说明
     */
    private String electronicInvoiceExplain;
    
    /**
     * 高热度节目 0:否 1:是
     * */
    private Integer highHeat;
    
    /**
     * 节目状态 1:上架 0:下架
     */
    private Integer programStatus;
    
    /**
     * 上架发行时间
     * */
    private Date issueTime;
}
