package com.damai.service.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.dto.SeatDto;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.service.composite.AbstractProgramCheckHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目订单参数检查
 * @author: 阿星不是程序员
 **/
@Component
public class ProgramOrderCreateParamCheckHandler extends AbstractProgramCheckHandler {
    
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<Long> ticketUserIdList = programOrderCreateDto.getTicketUserIdList();
        Map<Long, List<Long>> ticketUserIdMap = 
                ticketUserIdList.stream().collect(Collectors.groupingBy(ticketUserId -> ticketUserId));
        for (List<Long> value : ticketUserIdMap.values()) {
            if (value.size() > 1) {
                throw new DaMaiFrameException(BaseCode.TICKET_USER_ID_REPEAT);
            }
        }
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            if (seatDtoList.size() != programOrderCreateDto.getTicketUserIdList().size()) {
                throw new DaMaiFrameException(BaseCode.TICKET_USER_COUNT_UNEQUAL_SEAT_COUNT);
            }
            for (SeatDto seatDto : seatDtoList) {
                if (Objects.isNull(seatDto.getId())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_ID_EMPTY);
                }
                if (Objects.isNull(seatDto.getTicketCategoryId())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_TICKET_CATEGORY_ID_EMPTY);
                }
                if (Objects.isNull(seatDto.getRowCode())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_ROW_CODE_EMPTY);
                }
                if (Objects.isNull(seatDto.getColCode())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_COL_CODE_EMPTY);
                }
                if (Objects.isNull(seatDto.getPrice())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_PRICE_EMPTY);
                }
            }
        }else {
            if (Objects.isNull(programOrderCreateDto.getTicketCategoryId())) {
                throw new DaMaiFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST);
            }
            if (Objects.isNull(programOrderCreateDto.getTicketCount())) {
                throw new DaMaiFrameException(BaseCode.TICKET_COUNT_NOT_EXIST);
            }
            if (programOrderCreateDto.getTicketCount() <= 0) {
                throw new DaMaiFrameException(BaseCode.TICKET_COUNT_ERROR);
            }
        }
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
        return 1;
    }
}
