package com.example.servecase.service;

import com.example.servecase.dto.PayDto;
import com.example.servecase.entity.Pay;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-24
 **/
public interface IPayService {
    
    Pay getById(Long id);
    
    List<Pay> select(PayDto payDto);
    
    Integer insert(PayDto payDto);
    
    Integer updateById(Pay pay);
}
