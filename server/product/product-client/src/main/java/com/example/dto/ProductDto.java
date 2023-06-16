package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Data
public class ProductDto {
    
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer stock;
    
    /**
     * saveRedis为1存入redis
     * */
    private Integer saveRedis;
}
