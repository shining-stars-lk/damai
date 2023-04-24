package com.example.servecase.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
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