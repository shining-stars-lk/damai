package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Test {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Integer column1;

    private Integer column2;

    private Integer column3;

    private String column4;

    private String column5;

    private String column6;

    private Integer number;
}
