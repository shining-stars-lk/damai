package com.example.service.impl;

import com.example.dto.PayDto;
import com.example.entity.Account;
import com.example.entity.Pay;
import com.example.mapper.AccountMapper;
import com.example.mapper.PayMapper;
import com.example.service.IPayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/
@Service
@Transactional
public class PayService implements IPayService {
    
    @Autowired
    private PayMapper payMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountService accountService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public Integer testTransactionThread(Pay pay) {

        Long id = pay.getId();
        Future<Integer> future = executor.submit(() -> {
            Account account = new Account();
            account.setId(id);
            return accountService.insert(account);
        });
        int insert = payMapper.insert(pay);
        try {
            Integer integer = future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException("子线程account执行中断异常");
        } catch (ExecutionException e) {
            throw new RuntimeException("子线程account执行异常");
        }
        return insert;
    }

    @Override
    public Pay getById(Long id) {
        return payMapper.getById(id);
    }
    
    @Override
    public List<Pay> select(PayDto payDto) {
        return payMapper.select(payDto);
    }
    
    @Override
    public Integer insert(Pay pay) {
        return payMapper.insert(pay);
    }
    
    @Override
    public Integer updateById(final Pay record) {
        return payMapper.updateById(record);
    }


}
