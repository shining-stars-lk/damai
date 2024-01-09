package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.AreaGetDto;
import com.example.dto.AreaSelectDto;
import com.example.entity.Area;
import com.example.enums.AreaType;
import com.example.enums.BusinessStatus;
import com.example.mapper.AreaMapper;
import com.example.vo.AreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
                .eq(Area::getType, AreaType.MUNICIPALITIES.getCode())
                .or(wrapper -> wrapper
                        .eq(Area::getType, AreaType.PROVINCE.getCode())
                        .eq(Area::getMunicipality,BusinessStatus.YES.getCode()));
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }
    
    public List<AreaVo> selectByIdList(AreaSelectDto areaSelectDto) {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .in(Area::getId, areaSelectDto.getIdList());
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }
    
    public AreaVo getById(AreaGetDto areaGetDto) {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .eq(Area::getId, areaGetDto.getId());
        Area area = areaMapper.selectOne(lambdaQueryWrapper);
        AreaVo areaVo = new AreaVo();
        if (Objects.nonNull(area)) {
            BeanUtil.copyProperties(area,areaVo);
        }
        return areaVo;
    }
}
