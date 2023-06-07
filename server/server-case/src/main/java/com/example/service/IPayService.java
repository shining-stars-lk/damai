package com.example.service;

import com.example.dto.PayDto;
import com.example.entity.Pay;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
public interface IPayService {
    
    Pay getById(Long id);
    
    List<Pay> select(PayDto payDto);
    
    Integer insert(PayDto payDto);
    
    Integer updateById(Pay pay);
}
