package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.OrderCreateDto;
import com.example.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @ApiOperation(value = "创建订单")
    @PostMapping(value = "/create")
    public ApiResponse<String> create(OrderCreateDto orderCreateDto) {
        return ApiResponse.ok(orderService.create(orderCreateDto));
    }
}
