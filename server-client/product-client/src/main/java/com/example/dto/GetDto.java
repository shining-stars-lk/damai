package com.example.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Data
@ApiModel(value="GetDto", description ="订单")
public class GetDto {
    
    private String id;
    private String name;
    private Long sleepTime;
}
