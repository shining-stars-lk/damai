package com.damai.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户 返回vo
 * @author: 阿星不是程序员
 **/
@Data
public class UserVo {
    
    private String id;
    
    private String name;
    
    private String password;
    
    private Integer age;
    
    private Integer status;
    
    private Date createTime;
    
    private String mobile;
    
    private Date editTime;
}
