package com.damai.service.composite;

import com.damai.composite.AbstractComposite;
import com.damai.core.RedisKeyEnum;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.enums.CompositeCheckType;
import com.damai.exception.DaMaiFrameException;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
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
            throw new DaMaiFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST);
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
