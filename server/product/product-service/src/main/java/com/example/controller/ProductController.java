package com.example.controller;

import com.example.dto.GetDto;
import com.example.service.ProductService;
import com.example.vo.GetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping(value = "/get")
    public GetVo get(@RequestBody GetDto dto){
        return productService.get(dto);
    }
    
    @PostMapping(value = "/getV2")
    public GetVo getV2(@RequestBody GetDto dto){
        return productService.getV2(dto);
    }
}
