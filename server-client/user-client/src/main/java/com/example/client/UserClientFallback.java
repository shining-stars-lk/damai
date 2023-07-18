package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.UserDto;
import com.example.vo.UserVo;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
public class UserClientFallback implements UserClient {
    
    @Override
    public ApiResponse<UserVo> login(final UserDto userDto) {
        return null;
    }
}
