package com.example;

import lombok.Data;

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
    @NotNull
    private String productId;
    @NotNull
    private String productName;
    @NotNull
    private BigDecimal productPrice;
    @NotNull
    private Integer productAmount;
}
