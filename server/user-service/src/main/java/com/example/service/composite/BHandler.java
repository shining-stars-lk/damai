package com.example.service.composite;

import com.example.composite.CompositeInterface;
import com.example.dto.UserRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class BHandler extends CompositeInterface<UserRegisterDto> {
    
    
    @Override
    public void execute(final UserRegisterDto userRegisterDto) {
        System.out.println("B输出");
    }
    
    @Override
    public String type() {
        return "1";
    }
    
    @Override
    public Integer executeParentOrder() {
        return 1;
    }
    
    @Override
    public Integer executeTier() {
        return 2;
    }
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
}
