package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.ProgramCategoryAddDto;
import com.example.dto.ProgramCategoryDto;
import com.example.service.ProgramCategoryService;
import com.example.vo.ProgramCategoryVo;
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
 *  前端控制器
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@RestController
@RequestMapping("/program/category")
@Api(tags = "program/category", description = "节目类型")
public class ProgramCategoryController {
    
    @Autowired
    private ProgramCategoryService programCategoryService;
    
    @ApiOperation(value = "查询所有节目类型")
    @PostMapping(value = "/select/all")
    public ApiResponse<List<ProgramCategoryVo>> selectAll() {
        return ApiResponse.ok(programCategoryService.selectAll());
    }
    
    @ApiOperation(value = "通过类型查询节目类型")
    @PostMapping(value = "/selectByType")
    public ApiResponse<List<ProgramCategoryVo>> selectByType(@Valid @RequestBody ProgramCategoryDto programCategoryDto) {
        return ApiResponse.ok(programCategoryService.selectByType(programCategoryDto));
    }
    
    @ApiOperation(value = "批量添加节目类型")
    @PostMapping(value = "/save/batch")
    public ApiResponse<Void> saveBatch(@Valid @RequestBody List<ProgramCategoryAddDto> programCategoryAddDtoList) {
        programCategoryService.saveBatch(programCategoryAddDtoList);
        return ApiResponse.ok();
    }
}
