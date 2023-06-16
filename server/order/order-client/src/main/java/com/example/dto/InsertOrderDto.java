package com.example.dto;

import com.example.ProductDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Data
public class InsertOrderDto {

    @NotNull
    private List<ProductDto> productDtoList;
    @NotNull
    private BigDecimal payAmount;
    @NotNull
    private Integer payChannelType;
    
    private String userId;
}
