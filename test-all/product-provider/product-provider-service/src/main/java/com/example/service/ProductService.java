package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.dto.GetDto;
import com.example.vo.GetVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Service
@Log4j2
public class ProductService {
    
    
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
}
