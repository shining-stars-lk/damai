package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-07-10
 **/
@Slf4j
@RestController
@RequestMapping("/account")
@Api(tags = "account", description = "账户")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @ApiOperation(value = "通过id查询账户")
    @PostMapping(value = "getById")
    public Account getById(String id){
        return accountService.getById(id);
    }
}
