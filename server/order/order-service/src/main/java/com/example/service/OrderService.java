package com.example.service;

import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.example.ProductDto;
import com.example.client.ProductClient;
import com.example.common.Result;
import com.example.core.CacheKeyEnum;
import com.example.dto.GetDto;
import com.example.dto.GetOrderDto;
import com.example.dto.InsertOrderDto;
import com.example.entity.Order;
import com.example.entity.ProductOrder;
import com.example.enums.BaseCode;
import com.example.kafka.OrderMessageSend;
import com.example.redis.CacheKeyWrap;
import com.example.redis.DistributCache;
import com.example.vo.GetOrderVo;
import com.example.vo.GetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class OrderService {
    
    @Autowired
    private ProductClient productClient;
    
    @Autowired
    private DistributCache distributCache;
    
    @Autowired(required = false)
    private OrderMessageSend orderMessageSend;
    
    @Resource
    private UidGenerator uidGenerator;
    private DefaultRedisScript redisScript;
    
    @PostConstruct
    public void init(){
        try {
            redisScript = new DefaultRedisScript();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/decreaseProductStock.lua")));
            redisScript.setResultType(Long.class);
        } catch (Exception e) {
            log.error("redisScript init lua error",e);
        }
    }
    
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
    
    public Result<Boolean> insert(final InsertOrderDto dto) {
        List<ProductDto> productDtoList = dto.getProductDtoList();
        List<String> keyList = new ArrayList<>();
        String[] steps = new String[productDtoList.size()];
        for (int i = 0; i < productDtoList.size(); i++) {
            keyList.add(CacheKeyWrap.cacheKeyBuild(CacheKeyEnum.PRODUCT_STOCK, productDtoList.get(i).getProductId()).getRelKey());
            steps[i] = String.valueOf(productDtoList.get(i).getProductAmount());
        }
        long count = (Long) distributCache.getInstance().execute(redisScript, keyList, steps);
        if (count == -1) {
            return Result.error(BaseCode.PRODUCT_STOCK_NOT_ENOUGH);
        }
        if (orderMessageSend != null) {
            Order order = new Order();
            order.setId(String.valueOf(uidGenerator.getUID()));
            order.setPayAmount(dto.getPayAmount());
            order.setPayChannelType(dto.getPayChannelType());
            order.setCreateTime(new Date());
            order.setUserId(dto.getUserId());
            
            List<ProductOrder> productOrderList = new ArrayList<>();
            for (final ProductDto productDto : dto.getProductDtoList()) {
                ProductOrder productOrder = new ProductOrder();
                productOrder.setId(String.valueOf(uidGenerator.getUID()));
                productOrder.setProductId(productDto.getProductId());
                productOrder.setProductName(productDto.getProductName());
                productOrder.setProductPrice(productDto.getProductPrice());
                productOrder.setProductAmount(productDto.getProductAmount());
                productOrder.setProductTotalPrice(productDto.getProductPrice().multiply(new BigDecimal(productDto.getProductAmount())));
                productOrder.setOrderId(order.getId());
                productOrderList.add(productOrder);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("order",order);
            map.put("productOrderList",productOrderList);
            orderMessageSend.sendMessage(JSON.toJSONString(map));
        }
        return Result.success(true);
    }
}
