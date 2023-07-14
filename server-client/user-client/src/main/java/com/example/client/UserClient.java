package com.example.client;

import com.example.common.Result;
import com.example.dto.UserDto;
import com.example.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "user-service",fallback = UserClientFallback.class)
public interface UserClient {
    
    @GetMapping("/user/login")
    Result<UserVo> login(UserDto userDto);
}
