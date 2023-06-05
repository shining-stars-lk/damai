package com.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GetChannelDataByCodeDto {
    private Long id;

    private String name;

    private String code;

    private String introduce;

    private Date createTime;

    private Integer status;
    
    private Integer statusDel;
}