package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.AreaGetDto;
import com.example.dto.AreaSelectDto;
import com.example.service.AreaService;
import com.example.vo.AreaVo;
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
@RequestMapping("/area")
@Api(tags = "area", description = "区域")
public class AreaController {
    
    @Autowired
    private AreaService areaService;
    
    @ApiOperation(value = "查询市区以及直辖市数据")
    @PostMapping(value = "/selectCityData")
    public ApiResponse<List<AreaVo>> selectCityData() {
        return ApiResponse.ok(areaService.selectCityData());
    }
    
    @ApiOperation(value = "查询数据根据id集合")
    @PostMapping(value = "/selectByIdList")
    public ApiResponse<List<AreaVo>> selectByIdList(@Valid @RequestBody AreaSelectDto areaSelectDto) {
        return ApiResponse.ok(areaService.selectByIdList(areaSelectDto));
    }
    
    @ApiOperation(value = "查询数据根据id")
    @PostMapping(value = "/getById")
    public ApiResponse<AreaVo> getById(@Valid @RequestBody AreaGetDto areaGetDto){
        return ApiResponse.ok(areaService.getById(areaGetDto));
    }
}
