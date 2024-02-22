package com.damai.service.composite;

import com.damai.composite.AbstractComposite;
import com.damai.core.RedisKeyManage;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.enums.CompositeCheckType;
import com.damai.exception.DaMaiFrameException;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目演出时间检查
 * @author: 阿宽不是程序员
 **/
@Component
public class ProgramShowTimeCheckHandler extends AbstractComposite<ProgramOrderCreateDto> {
    
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        //查询节目演出时间
        ProgramShowTime programShowTime = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME
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
        return 1;
    }
    
    @Override
    public Integer executeTier() {
        return 4;
    }
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
}
