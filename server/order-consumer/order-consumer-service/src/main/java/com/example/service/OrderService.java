package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.client.ProductClient;
import com.example.dto.GetDto;
import com.example.dto.GetOrderDto;
import com.example.vo.GetOrderVo;
import com.example.vo.GetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class OrderService {
    
    @Autowired
    private ProductClient productClient;
    
    public GetOrderVo getOrder(final GetOrderDto getOrderDto) {
        GetOrderVo getOrderVo = new GetOrderVo();
        getOrderVo.setId(getOrderDto.getId());
        getOrderVo.setName("苹果订单");
        
        GetDto getDto = new GetDto();
        getDto.setId(getOrderDto.getId() + "11");
        if (getOrderDto.getSleepTime() != null) {
            getDto.setSleepTime(getOrderDto.getSleepTime());
        }
        GetVo getVo = productClient.get(getDto);
        if (getVo != null) {
            getOrderVo.setProductId(getVo.getId());
            getOrderVo.setProductName(getVo.getName());
            getOrderVo.setProductNumber(getVo.getNumber());
        }
        log.info("getOrder执行 GetOrderVo : {}", JSON.toJSONString(getOrderVo));
        return getOrderVo;
    }
    
    public GetOrderVo getOrderV2(final GetOrderDto getOrderDto) {
        GetOrderVo getOrderVo = new GetOrderVo();
        getOrderVo.setId(getOrderDto.getId());
        getOrderVo.setName("橘子订单");
    
        GetDto getDto = new GetDto();
        getDto.setId(getOrderDto.getId() + "22");
        if (getOrderDto.getSleepTime() != null) {
            getDto.setSleepTime(getOrderDto.getSleepTime());
        }
        GetVo getVo = productClient.getV2(getDto);
        if (getVo != null) {
            getOrderVo.setProductId(getVo.getId());
            getOrderVo.setProductName(getVo.getName());
            getOrderVo.setProductNumber(getVo.getNumber());
        }
        log.info("getOrderV2执行 GetOrderVo : {}", JSON.toJSONString(getOrderVo));
        return getOrderVo;
    }
}
