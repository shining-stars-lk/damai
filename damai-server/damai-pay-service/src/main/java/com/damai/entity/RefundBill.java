package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseTableData;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 退款账单 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_refund_bill")
public class RefundBill extends BaseTableData {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商户订单号
     */
    private String outOrderNo;
    
    /**
     * 账单id
     */
    private Long payBillId;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 账单退款状态 1：未退款 2：已退款
     */
    private Integer refundStatus;

    /**
     * 退款时间
     */
    private Date refundTime;
    
    /**
     * 退款原因
     * */
    private String reason;
}
