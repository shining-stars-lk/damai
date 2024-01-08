package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.common.ApiResponse;
import com.example.dto.ProgramDto;
import com.example.service.ProgramService;
import com.example.vo.ProgramListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 节目表 前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/program")
@Api(tags = "program", description = "节目")
public class ProgramController {
    
    @Autowired
    private ProgramService programService;
    
    @ApiOperation(value = "查询列表")
    @PostMapping(value = "/selectPage")
    public ApiResponse<IPage<ProgramListVo>> selectPage(@Valid @RequestBody ProgramDto programDto) {
        return ApiResponse.ok(programService.selectPage(programDto));
    }
}
