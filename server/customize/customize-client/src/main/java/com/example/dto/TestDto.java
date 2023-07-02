package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: bjgoodwill-msa-scloud
 * @description:
 * @author: lk
 * @create: 2023-06-07
 **/
@Data
public class TestDto {
    
    @NotBlank
    private String id;
}
