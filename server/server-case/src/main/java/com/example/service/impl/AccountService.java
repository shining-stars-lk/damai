package com.example.service.impl;

import com.example.entity.Account;
import com.example.mapper.AccountMapper;
import com.example.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Integer insert(Account account) {
        Integer insert = accountMapper.insert(account);
        if (account.getId() == 2) {
            throw new RuntimeException("模拟异常");
        }
        return insert;
    }
}
