package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Account {

    private Long id;

    private String name;

    private String password;

    private Integer age;

    private Integer status;

    private Date createTime;
}
