package com.example.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Pay {
    private Long id;

    private String userId;

    private Integer type;

    private Integer status;

    private BigDecimal amount;

    private Integer origin;

    private Date payTime;

    private String departmentId;

    private Date createTime;

    private Integer refundStatus;

    private BigDecimal refundAmount;

    private Date refundTime;

    private String refundMark;
}