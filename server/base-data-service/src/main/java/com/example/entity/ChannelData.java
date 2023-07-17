package com.example.entity;

import lombok.Data;

import java.util.Date;
@Data
public class ChannelData {
    
    private String id;

    private String name;

    private String code;

    private String introduce;

    private Date createTime;

    private Integer status;
    
    private String signPublicKey;
    
    private String signSecretKey;
    
    private String aesKey;
}