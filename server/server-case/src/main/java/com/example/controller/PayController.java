package com.example.controller;

import com.example.dto.PayDto;
import com.example.entity.Pay;
import com.example.service.impl.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: 测试if标签失效造成内存溢出
 * @description:
 * @author: k
 * @create: 2023-04-24
 **/

@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {
    
    @Autowired
    private PayService payService;
    
    @PostMapping("/insert")
    public Integer insert(@RequestBody PayDto payDto){
        return payService.insert(payDto);
    }
    
    @PostMapping("/getById/{id}")
    public Pay getById(@PathVariable Long id) {
        return payService.getById(id);
    }
    
    @PostMapping("/select")
    public List<Pay> select(@RequestBody PayDto payDto){
        return payService.select(payDto);
    }
}
