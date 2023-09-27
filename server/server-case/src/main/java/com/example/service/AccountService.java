package com.example.service;

import com.example.entity.Account;
import com.example.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;
    
    public Account getById(String id){
        if ("1".equals(id)) {
            throw new RuntimeException("自定义异常");
        }
        return new Account();
//        return accountMapper.selectById(id);
    }
}
