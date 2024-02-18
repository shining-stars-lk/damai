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
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户服务 feign
 * @author: 阿宽不是程序员
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
