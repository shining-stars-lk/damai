package com.damai.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.core.RedisKeyManage;
import com.damai.dto.AreaGetDto;
import com.damai.dto.AreaSelectDto;
import com.damai.entity.Area;
import com.damai.enums.AreaType;
import com.damai.enums.BusinessStatus;
import com.damai.mapper.AreaMapper;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.vo.AreaVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 地区 service
 * @author: 阿星不是程序员
 **/
@Slf4j
@Service
public class AreaService extends ServiceImpl<AreaMapper, Area> {
    
    @Autowired
    private AreaMapper areaMapper;
   
    @Autowired
    private RedisCache redisCache;
    
    public List<AreaVo> selectCityData() {
        List<AreaVo> areaVos = redisCache.rangeForList(RedisKeyBuild.createRedisKey(RedisKeyManage.AREA_PROVINCE_LIST),
                0, -1, AreaVo.class);
        if (CollectionUtil.isEmpty(areaVos)) {
            final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                    .eq(Area::getType, AreaType.MUNICIPALITIES.getCode())
                    .or(wrapper -> wrapper
                            .eq(Area::getType, AreaType.PROVINCE.getCode())
                            .eq(Area::getMunicipality,BusinessStatus.YES.getCode()));
            List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
            areaVos = BeanUtil.copyToList(areas,AreaVo.class);
            if (CollectionUtil.isNotEmpty(areaVos)) {
                redisCache.leftPushAllForList(RedisKeyBuild.createRedisKey(RedisKeyManage.AREA_PROVINCE_LIST),areaVos);
            }
        }
        return areaVos;
    }
    
    public List<AreaVo> selectByIdList(AreaSelectDto areaSelectDto) {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .in(Area::getId, areaSelectDto.getIdList());
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }
    
    public AreaVo getById(AreaGetDto areaGetDto) {
        log.info("基础服务调用 getById:{}", JSON.toJSONString(areaGetDto));
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .eq(Area::getId, areaGetDto.getId());
        Area area = areaMapper.selectOne(lambdaQueryWrapper);
        AreaVo areaVo = new AreaVo();
        if (Objects.nonNull(area)) {
            BeanUtil.copyProperties(area,areaVo);
        }
        return areaVo;
    }
    
    public AreaVo current() {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .eq(Area::getId, 2);
        Area area = areaMapper.selectOne(lambdaQueryWrapper);
        AreaVo areaVo = new AreaVo();
        if (Objects.nonNull(area)) {
            BeanUtil.copyProperties(area,areaVo);
        }
        return areaVo;
    }
    
    public List<AreaVo> hot() {
        final LambdaQueryWrapper<Area> lambdaQueryWrapper = Wrappers.lambdaQuery(Area.class)
                .in(Area::getName, "全国","北京","上海","深圳","广州","杭州","天津","重庆","成都","中国香港");
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }
}
