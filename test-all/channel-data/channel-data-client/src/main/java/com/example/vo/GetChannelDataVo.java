package com.example.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GetChannelDataVo {
    private String id;

    private String name;

    private String code;

    private String introduce;

    private Date createTime;

    private Integer status;
    
    private String publicKey;
    
    private String secretKey;
    
    private String aesKey;
}