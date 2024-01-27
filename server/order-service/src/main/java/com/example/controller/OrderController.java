package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.OrderCancelDto;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderGetDto;
import com.example.dto.OrderPayCheckDto;
import com.example.dto.OrderPayDto;
import com.example.service.OrderService;
import com.example.vo.OrderGetVo;
import com.example.vo.OrderPayCheckVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 订单类型表 前端控制器
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
    
    @ApiOperation(value = "订单支付后状态检查")
    @PostMapping(value = "/pay/check")
    public ApiResponse<OrderPayCheckVo> payCheck(@Valid @RequestBody OrderPayCheckDto orderPayCheckDto) {
        return ApiResponse.ok(orderService.payCheck(orderPayCheckDto));
    }
    
    @ApiOperation(value = "支付宝支付后回调通知")
    @PostMapping(value = "/alipay/notify")
    public String alipayNotify(@RequestParam Map<String, String> params) {
        return orderService.alipayNotify(params,params.get("out_trade_no"));
    }
    
    @ApiOperation(value = "订单取消")
    @PostMapping(value = "/cancel")
    public ApiResponse<Boolean> cancel(@Valid @RequestBody OrderCancelDto orderCancelDto) {
        return ApiResponse.ok(orderService.cancel(orderCancelDto));
    }
    
    @ApiOperation(value = "订单详情")
    @PostMapping(value = "/get")
    public ApiResponse<OrderGetVo> get(@Valid @RequestBody OrderGetDto orderGetDto) {
        return ApiResponse.ok(orderService.get(orderGetDto));
    }
}
