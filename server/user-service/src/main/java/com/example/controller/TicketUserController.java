package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.TicketUserDto;
import com.example.dto.TicketUserIdDto;
import com.example.dto.TicketUserListDto;
import com.example.service.TicketUserService;
import com.example.vo.TicketUserVo;
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
 * <p>
 * 购票人表 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-09
 */
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
