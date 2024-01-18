package com.example.service.composite;

import com.example.composite.CompositeInterface;
import com.example.dto.UserRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class AHandler extends CompositeInterface<UserRegisterDto> {
    
    
    @Override
    public void execute(final UserRegisterDto userRegisterDto) {
        System.out.println("A输出");
    }
    
    @Override
    public String type() {
        return "1";
    }
    
    @Override
    public Integer executeParentOrder() {
        return 0;
    }
    
    @Override
    public Integer executeTier() {
        return 1;
    }
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
}
