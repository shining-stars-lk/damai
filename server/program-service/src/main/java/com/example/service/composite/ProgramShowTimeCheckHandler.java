package com.example.service.composite;

import com.example.composite.AbstractComposite;
import com.example.core.RedisKeyEnum;
import com.example.dto.ProgramOrderCreateDto;
import com.example.entity.ProgramShowTime;
import com.example.enums.BaseCode;
import com.example.enums.CompositeCheckType;
import com.example.exception.CookFrameException;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-22
 **/
@Component
public class ProgramShowTimeCheckHandler extends AbstractComposite<ProgramOrderCreateDto> {
    
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        //查询节目演出时间
        ProgramShowTime programShowTime = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SHOW_TIME
                ,programOrderCreateDto.getProgramId()),ProgramShowTime.class);
        if (Objects.isNull(programShowTime)) {
            throw new CookFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST);
        }
    }
    
    @Override
    public String type() {
        return CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue();
    }
    
    @Override
    public Integer executeParentOrder() {
        return 0;
    }
    
    @Override
    public Integer executeTier() {
        return 1;
    }
    
    @Override
    public Integer executeOrder() {
        return 4;
    }
}
