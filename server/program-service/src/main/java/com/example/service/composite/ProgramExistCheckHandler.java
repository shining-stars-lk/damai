package com.example.service.composite;

import com.example.composite.AbstractComposite;
import com.example.core.RedisKeyEnum;
import com.example.dto.ProgramOrderCreateDto;
import com.example.enums.BaseCode;
import com.example.enums.CompositeCheckType;
import com.example.exception.CookFrameException;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.vo.ProgramVo;
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
public class ProgramExistCheckHandler extends AbstractComposite<ProgramOrderCreateDto> {
    
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        //查询要购买的节目
        ProgramVo programVo = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM, programOrderCreateDto.getProgramId()), ProgramVo.class);
        if (Objects.isNull(programVo)) {
            throw new CookFrameException(BaseCode.PROGRAM_NOT_EXIST);
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
        return 2;
    }
}
