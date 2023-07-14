package com.example.service;

import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.example.core.RedisKeyEnum;
import com.example.dto.GetDto;
import com.example.dto.ProductDto;
import com.example.entity.Product;
import com.example.mapper.ProductMapper;
import com.example.redis.RedisKeyWrap;
import com.example.redis.RedisCache;
import com.example.vo.GetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@Service
@Slf4j
public class ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    @Resource
    private UidGenerator uidGenerator; 
    
    
    private Integer number1 = 10000;
    
    private Integer number2 = 999;
    
    public GetVo get(GetDto dto){
        if (dto.getSleepTime() != null) {
            try {
                Thread.sleep(dto.getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        GetVo getVo = new GetVo();
        getVo.setId(dto.getId());
        getVo.setName("苹果");
        getVo.setNumber(number1);
        log.info("get执行 GetVo : {}", JSON.toJSONString(getVo));
        return getVo;
    }
    
    public GetVo getV2(GetDto dto) {
        if (dto.getSleepTime() != null) {
            try {
                Thread.sleep(dto.getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        GetVo getVo = new GetVo();
        getVo.setId(dto.getId());
        getVo.setName("橘子");
        getVo.setNumber(number2);
        log.info("get执行 GetVo : {}", JSON.toJSONString(getVo));
        return getVo;
    }
    
    public Boolean insert(final ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto,product);
        product.setId(String.valueOf(uidGenerator.getUID()));
        product.setCreateTime(new Date());
        
        if (productDto.getSaveRedis() != null && productDto.getSaveRedis() == 1) {
            productMapper.insert(product);
        }
        redisCache.incrBy(RedisKeyWrap.createRedisKey(RedisKeyEnum.PRODUCT_STOCK,String.valueOf(product.getId())),product.getStock());
        return true;
    }
}
