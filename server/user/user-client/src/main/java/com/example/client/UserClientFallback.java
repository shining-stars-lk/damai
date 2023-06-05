package com.example.client;

import com.example.common.Result;
import com.example.dto.UserDto;
import com.example.vo.UserVo;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Component
public class UserClientFallback implements UserClient {
    
    @Override
    public Result<UserVo> login(final UserDto userDto) {
        return null;
    }
}
