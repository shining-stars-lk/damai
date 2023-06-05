package com.example.controller;

import com.example.dto.GetOrderDto;
import com.example.service.OrderService;
import com.example.vo.GetOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping(value = "/getOrder")
    public GetOrderVo getOrder(@RequestBody GetOrderDto getOrderDto){
        return orderService.getOrder(getOrderDto);
    }
    
    @PostMapping(value = "/getOrderV2")
    public GetOrderVo getOrderV2(@RequestBody GetOrderDto getOrderDto){
        return orderService.getOrderV2(getOrderDto);
    }
}
