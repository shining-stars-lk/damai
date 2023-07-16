package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.GetDto;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import com.example.vo.GetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping(value = "/get")
    public GetVo get(@Valid @RequestBody GetDto dto){
        return productService.get(dto);
    }
    
    @PostMapping(value = "/getV2")
    public GetVo getV2(@Valid @RequestBody GetDto dto){
        return productService.getV2(dto);
    }
    
    @PostMapping(value = "/insert")
    public ApiResponse<Boolean> insert(@Valid @RequestBody ProductDto productDto){
        Boolean result = productService.insert(productDto);
        return ApiResponse.ok(result);
    }
}
