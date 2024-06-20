package com.damai.service.composite.impl;

import com.damai.dto.ProgramOrderCreateDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.service.ProgramShowTimeService;
import com.damai.service.SeatService;
import com.damai.service.composite.AbstractProgramCheckHandler;
import com.damai.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 将节目座位缓存
 * @author: 阿星不是程序员
 **/
@Component
public class ProgramSeatCacheHandler extends AbstractProgramCheckHandler {
    
    @Autowired
    private ProgramShowTimeService programShowTimeService;
    
    @Autowired
    private SeatService seatService;
    
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        ProgramShowTime programShowTime =
                programShowTimeService.selectProgramShowTimeByProgramIdMultipleCache(programOrderCreateDto.getProgramId());
        if (Objects.isNull(programShowTime)) {
            throw new DaMaiFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST);
        }
        
        seatService.selectSeatByProgramId(programOrderCreateDto.getProgramId(),
                DateUtils.countBetweenSecond(DateUtils.now(), programShowTime.getShowTime()), TimeUnit.SECONDS);
    }
    
    @Override
    public Integer executeParentOrder() {
        return 1;
    }
    
    @Override
    public Integer executeTier() {
        return 2;
    }
    
    @Override
    public Integer executeOrder() {
        return 2;
    }
}
