//package com.example.service.composite;
//
//import com.example.composite.CompositeInterface;
//import com.example.dto.UserRegisterDto;
//import com.example.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserExistCheckHandler extends CompositeInterface<UserRegisterDto> {
//    
//    @Autowired
//    private UserService userService;
//    
//    @Override
//    public void execute(final UserRegisterDto userRegisterDto) {
//        userService.doExist(userRegisterDto.getMobile());
//    }
//    
//    @Override
//    public String type() {
//        return "1";
//    }
//    
//    @Override
//    public Integer executeTier() {
//        return 1;
//    }
//    
//    @Override
//    public Integer executeOrder() {
//        return 1;
//    }
//}
