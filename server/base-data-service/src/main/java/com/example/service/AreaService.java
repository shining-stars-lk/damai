package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.AreaDto;
import com.example.entity.Area;
import com.example.enums.AreaType;
import com.example.enums.BusinessStatus;
import com.example.mapper.AreaMapper;
import com.example.vo.AreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@Service
public class AreaService extends ServiceImpl<AreaMapper, Area> {
    
    @Autowired
    private AreaMapper areaMapper;
    
    public List<AreaVo> selectCityData() {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .eq(Area::getType, AreaType.municipalities.getCode())
                .or(wrapper -> wrapper
                        .eq(Area::getType, AreaType.province.getCode())
                        .eq(Area::getMunicipality,BusinessStatus.YES.getCode()));
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }
    
    public List<AreaVo> selectByIdList(AreaDto areaDto) {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .in(Area::getId, areaDto.getIdList());
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }
}
