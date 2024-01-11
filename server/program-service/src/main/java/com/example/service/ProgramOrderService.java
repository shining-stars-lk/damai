package com.example.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.ProgramOrderDto;
import com.example.dto.SeatDto;
import com.example.entity.Program;
import com.example.entity.Seat;
import com.example.enums.BaseCode;
import com.example.enums.SellStatus;
import com.example.exception.CookFrameException;
import com.example.mapper.ProgramMapper;
import com.example.mapper.SeatMapper;
import com.example.mapper.TicketCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-11
 **/
@Service
public class ProgramOrderService {
    
    @Autowired
    private ProgramMapper programMapper;
    
    @Autowired
    private SeatMapper seatMapper;
    
    @Autowired
    private TicketCategoryMapper ticketCategoryMapper;
    
    public void create(final ProgramOrderDto programOrderDto) {
        Program program = programMapper.selectById(programOrderDto.getProgramId());
        if (Objects.isNull(program)) {
            throw new CookFrameException(BaseCode.PROGRAM_NOT_EXIST);
        }
        //todo 座位号的选择，这里先写死
        final List<SeatDto> seatDtoList = programOrderDto.getSeatDtoList();
        
        Seat updateSeat = new Seat();
        updateSeat.setSellStatus(SellStatus.LOCK.getCode());
        LambdaUpdateWrapper<Seat> seatLambdaUpdateWrapper = Wrappers.lambdaUpdate(Seat.class)
                .eq(Seat::getSellStatus,SellStatus.NO_SOLD.getCode());
        for (SeatDto seatDto : seatDtoList) {
            seatLambdaUpdateWrapper.or(i -> i.eq(Seat::getColCode,seatDto.getColCode()).eq(Seat::getRowCode,seatDto.getRowCode()));
        }
        //锁座位
        seatMapper.update(updateSeat, seatLambdaUpdateWrapper);
        
        //扣票数
        Map<Long, Long> ticketCategoryCount = seatDtoList.stream()
                .collect(Collectors.groupingBy(SeatDto::getTicketCategoryId, Collectors.counting()));
        for (final Entry<Long, Long> entry : ticketCategoryCount.entrySet()) {
            final Long key = entry.getKey();
            final Long value = entry.getValue();
            ticketCategoryMapper.updateRemainNumber(key,value);   
        }
    }
}
