package com.damai.client;

import com.damai.common.ApiResponse;
import com.damai.dto.UserGetAndTicketUserListDto;
import com.damai.dto.UserIdDto;
import com.damai.vo.UserGetAndTicketUserListVo;
import com.damai.vo.TicketUserVo;
import com.damai.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Component
@FeignClient(value = "user-service",fallback = UserClientFallback.class)
public interface UserClient {
    
    /**
     * 查询用户(通过id)
     * */
    @PostMapping(value = "/user/getById")
    ApiResponse<UserVo> getById(UserIdDto userIdDto);
    

    /**
     * 查询购票人(通过userId)
     * */
    @PostMapping(value = "/ticket/user/select")
    ApiResponse<List<TicketUserVo>> select(@Valid @RequestBody UserIdDto userIdDto);
    
    /**
     * 查询用户和购票人集合
     */
    @PostMapping(value = "/user/getUserAndTicketUserList")
    ApiResponse<UserGetAndTicketUserListVo> getUserAndTicketUserList(UserGetAndTicketUserListDto userGetAndTicketUserListDto);
    
}
