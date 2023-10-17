package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.GetLockDataDto;
import com.example.dto.LockDataDto;
import com.example.entity.LockData;
import com.example.mapper.LockDataMapper;
import com.example.servicelock.annotion.ServiceLock;
import com.example.vo.LockDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.core.DistributedLockConstants.LOCK_DATA;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Slf4j
@Service
public class LockDataService {
    
    @Autowired
    private LockDataMapper lockDataMapper;
    
    public LockDataVo get(GetLockDataDto getLockDataDto) {
        LockDataVo lockDataVo = new LockDataVo();
        Optional.ofNullable(lockDataMapper.selectById(getLockDataDto.getId())).ifPresent(lockData -> {
            BeanUtils.copyProperties(lockData,lockDataVo);
        });
        return lockDataVo;
    }
    
    @Transactional
    public void addServiceLock(LockDataDto lockDataDto){
        LockData lockData = new LockData();
        BeanUtils.copyProperties(lockDataDto,lockData);
        lockDataMapper.insert(lockData);
    }
    
    @ServiceLock(name = LOCK_DATA,keys = {"#lockDataDto.id"},waitTime = 5L)
    @Transactional
    public void addServiceLockStock(LockDataDto lockDataDto){
        LambdaQueryWrapper<LockData> wrapper = Wrappers.lambdaQuery(LockData.class)
                .eq(LockData::getId, lockDataDto.getId());
        lockDataMapper.selectOne(wrapper);
        Optional.ofNullable(lockDataMapper.selectOne(wrapper)).ifPresent(lockData -> {
            Integer newStock = lockData.getStock() + lockDataDto.getStock();
            log.info("id为{}的当前库存为:{},添加后的库存为:{}",lockData.getId(),lockData.getStock(),newStock);
            lockData.setStock(newStock);
            lockDataMapper.updateById(lockData);
        });
    }
    
    
}
