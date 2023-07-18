package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.PsOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-14
 **/
public interface OrderMapper extends BaseMapper<PsOrder> {
    
    int timeOutCancelOrder(@Param("id")String id);
}
