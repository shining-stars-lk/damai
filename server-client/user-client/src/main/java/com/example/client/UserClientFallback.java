package com.example.client;

import com.example.common.ApiResponse;
import com.example.dto.UserGetAndTicketUserListDto;
import com.example.dto.UserIdDto;
import com.example.enums.BaseCode;
import com.example.vo.UserGetAndTicketUserListVo;
import com.example.vo.TicketUserVo;
import com.example.vo.UserVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
public class UserClientFallback implements UserClient {
    
    @Override
    public ApiResponse<UserVo> getById(final UserIdDto userIdDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<List<TicketUserVo>> select(final UserIdDto userIdDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
    
    @Override
    public ApiResponse<UserGetAndTicketUserListVo> getUserAndTicketUserList(final UserGetAndTicketUserListDto userGetAndTicketUserListDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
