package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.UserGetAndTicketUserListDto;
import com.damai.dto.UserIdDto;
import com.damai.enums.BaseCode;
import com.damai.vo.UserGetAndTicketUserListVo;
import com.damai.vo.TicketUserVo;
import com.damai.vo.UserVo;
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
