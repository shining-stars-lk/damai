package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.OrderCancelDto;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderPayDto;
import com.example.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 节目类型表 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-12
 */
@RestController
@RequestMapping("/order")
@Api(tags = "order", description = "订单")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @ApiOperation(value = "订单创建")
    @PostMapping(value = "/create")
    public ApiResponse<String> create(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        return ApiResponse.ok(orderService.create(orderCreateDto));
    }
    
    @ApiOperation(value = "订单支付")
    @PostMapping(value = "/pay")
    public ApiResponse<String> pay(@Valid @RequestBody OrderPayDto orderPayDto) {
        return ApiResponse.ok(orderService.pay(orderPayDto));
    }
    
    @ApiOperation(value = "订单取消")
    @PostMapping(value = "/cancel")
    public ApiResponse<Boolean> cancel(@Valid @RequestBody OrderCancelDto orderCancelDto) {
        return ApiResponse.ok(orderService.cancel(orderCancelDto));
    }
}
