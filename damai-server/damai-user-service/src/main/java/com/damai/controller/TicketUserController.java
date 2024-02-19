package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.TicketUserDto;
import com.damai.dto.TicketUserIdDto;
import com.damai.dto.TicketUserListDto;
import com.damai.service.TicketUserService;
import com.damai.vo.TicketUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 购票人 控制层
 * @author: 阿宽不是程序员
 **/
@RestController
@RequestMapping("/ticket/user")
@Api(tags = "ticket-user", description = "购票人")
public class TicketUserController {
    
    @Autowired
    private TicketUserService ticketUserService;
    
    @ApiOperation(value = "查询购票人列表")
    @PostMapping(value = "/select")
    public ApiResponse<List<TicketUserVo>> select(@Valid @RequestBody TicketUserListDto ticketUserListDto){
        return ApiResponse.ok(ticketUserService.select(ticketUserListDto));
    }
    
    @ApiOperation(value = "添加购票人")
    @PostMapping(value = "/add")
    public ApiResponse<Void> add(@Valid @RequestBody TicketUserDto ticketUserDto){
        ticketUserService.add(ticketUserDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "删除购票人")
    @PostMapping(value = "/delete")
    public ApiResponse<Void> delete(@Valid @RequestBody TicketUserIdDto ticketUserIdDto){
        ticketUserService.delete(ticketUserIdDto);
        return ApiResponse.ok();
    }
}
