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

    Integer testTransactionThread(Pay pay);
    
    Pay getById(Long id);
    
    List<Pay> select(PayDto payDto);
    
    Integer insert(Pay pay);
    
    Integer updateById(Pay pay);
}
