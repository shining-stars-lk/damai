package com.example.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-05
 **/

@Data
public class GetDeptVo {
    
    private String id;
    
    private String name;
    
    private String typeCode;
    
    private Date createTime;
    
    private Integer status;
}
