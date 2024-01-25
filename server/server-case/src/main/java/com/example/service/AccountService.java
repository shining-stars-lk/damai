package com.example.service;

import com.example.entity.Account;
import com.example.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;
    
    public Account getById(String id){
        return accountMapper.selectById(id);
    }
}
