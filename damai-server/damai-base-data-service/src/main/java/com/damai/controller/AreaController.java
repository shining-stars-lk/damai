package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.service.AreaService;
import com.damai.vo.AreaVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 地区 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/area")
@Tag(name = "area", description = "区域")
public class AreaController {
    
    @Autowired
    private AreaService areaService;
    
    @Operation(summary  = "查询市区以及直辖市数据")
    @PostMapping(value = "/selectCityData")
    public ApiResponse<List<AreaVo>> selectCityData() {
        return ApiResponse.ok(areaService.selectCityData());
    }
    
    @Operation(summary  = "查询数据根据id集合")
    @PostMapping(value = "/selectByIdList")
    public ApiResponse<List<AreaVo>> selectByIdList(@Valid @RequestBody AreaSelectDto areaSelectDto) {
        return ApiResponse.ok(areaService.selectByIdList(areaSelectDto));
    }
    
    @Operation(summary  = "查询数据根据id")
    @PostMapping(value = "/getById")
    public ApiResponse<AreaVo> getById(@Valid @RequestBody AreaGetDto areaGetDto){
        return ApiResponse.ok(areaService.getById(areaGetDto));
    }
    
    @Operation(summary  = "当前城市")
    @PostMapping(value = "/current")
    public ApiResponse<AreaVo> current(){
        return ApiResponse.ok(areaService.current());
    }
    
    @Operation(summary  = "热门城市")
    @PostMapping(value = "/hot")
    public ApiResponse<List<AreaVo>> hot() {
        return ApiResponse.ok(areaService.hot());
    }
}
