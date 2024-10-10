package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-10-10
 **/
@Data
@TableName("test1")
public class Test1 {
    
    private Long id;
    
    private String code;
}
