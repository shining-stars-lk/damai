package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.ProgramResetExecuteDto;
import com.damai.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目相关数据重置 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/program/reset")
@Tag(name = "program-reset", description = "节目数据重置")
public class ProgramResetController {
    
    @Autowired
    private ProgramService programService;
    
    @Operation(summary  = "执行重置(根据节目id)")
    @PostMapping(value = "/execute")
    public ApiResponse<Boolean> resetExecute(@Valid @RequestBody ProgramResetExecuteDto programResetExecuteDto) {
        return ApiResponse.ok(programService.resetExecute(programResetExecuteDto));
    }
}
