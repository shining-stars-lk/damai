package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.PayDto;
import com.example.dto.TradeCheckDto;
import com.example.service.PayService;
import com.example.vo.TradeCheckVo;
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
 * 支付账单表 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/pay")
@Api(tags = "pay", description = "支付")
public class PayController {
    
    @Autowired
    private PayService payService;
    
    @ApiOperation(value = "支付")
    @PostMapping(value = "/common/pay")
    public ApiResponse<String> commonPay(@Valid @RequestBody PayDto payDto) {
        return ApiResponse.ok(payService.commonPay(payDto));
    }
    
    @ApiOperation(value = "支付宝支付后通知")
    @PostMapping(value = "/alipay/notify")
    public ApiResponse<String> alipayNotify(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(payService.alipayNotify(params,params.get("out_trade_no")));
    }
    
    @ApiOperation(value = "支付状态查询")
    @PostMapping(value = "/trade/check")
    public ApiResponse<TradeCheckVo> tradeCheck(@Valid @RequestBody TradeCheckDto tradeCheckDto) {
        return ApiResponse.ok(payService.tradeCheck(tradeCheckDto));
    }
}
