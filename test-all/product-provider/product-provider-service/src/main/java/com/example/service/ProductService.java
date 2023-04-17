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
    
    
    private Integer number = 10000;
    
    public GetVo get(GetDto dto){
        GetVo getVo = new GetVo();
        getVo.setId(dto.getId());
        getVo.setName("苹果");
        getVo.setNumber(number);
        log.info("get执行 GetVo : {}", JSON.toJSONString(getVo));
        return getVo;
    }
}
