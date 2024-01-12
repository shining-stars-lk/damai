package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderTicketUserCreateDto;
import com.example.dto.ProgramOrderCreateDto;
import com.example.dto.ProgramOrderTicketUserDto;
import com.example.dto.SeatDto;
import com.example.entity.Program;
import com.example.entity.Seat;
import com.example.enums.BaseCode;
import com.example.enums.SellStatus;
import com.example.exception.CookFrameException;
import com.example.mapper.ProgramMapper;
import com.example.mapper.SeatMapper;
import com.example.mapper.TicketCategoryMapper;
import com.example.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
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
    
    @Autowired
    private UidGenerator uidGenerator;
    
    public void create(final ProgramOrderCreateDto programOrderCreateDto) {
        final List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        if (CollectionUtil.isEmpty(seatDtoList) && Objects.isNull(programOrderCreateDto.getTicketCategoryId())) {
            throw new CookFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST);
        }
        
        Program program = programMapper.selectById(programOrderCreateDto.getProgramId());
        if (Objects.isNull(program)) {
            throw new CookFrameException(BaseCode.PROGRAM_NOT_EXIST);
        }
        
        
        Seat updateSeat = new Seat();
        updateSeat.setSellStatus(SellStatus.LOCK.getCode());
        LambdaUpdateWrapper<Seat> seatLambdaUpdateWrapper = Wrappers.lambdaUpdate(Seat.class)
                .eq(Seat::getSellStatus,SellStatus.NO_SOLD.getCode());
        
        BigDecimal orderPrice = new BigDecimal("0");
        
        for (SeatDto seatDto : seatDtoList) {
            orderPrice = orderPrice.add(seatDto.getPrice());
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
        
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setId(uidGenerator.getUID());
        orderCreateDto.setProgramId(programOrderCreateDto.getProgramId());
        orderCreateDto.setUserId(programOrderCreateDto.getUserId());
        orderCreateDto.setOrderPrice(orderPrice);
        orderCreateDto.setCreateOrderTime(DateUtils.now());
        
        List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList = new ArrayList<>();
        List<ProgramOrderTicketUserDto> programOrderTicketUserDtoList = programOrderCreateDto.getProgramOrderTicketUserDtoList();
        for (int i = 0; i < programOrderTicketUserDtoList.size(); i++) {
            ProgramOrderTicketUserDto programOrderTicketUserDto = programOrderTicketUserDtoList.get(i);
            OrderTicketUserCreateDto orderTicketUserCreateDto = new OrderTicketUserCreateDto();
            orderTicketUserCreateDto.setOrderId(orderCreateDto.getId());
            orderTicketUserCreateDto.setProgramId(programOrderCreateDto.getProgramId());
            orderTicketUserCreateDto.setUserId(programOrderCreateDto.getUserId());
            orderTicketUserCreateDto.setTicketUserId(programOrderTicketUserDto.getId());
            SeatDto seatDto = Optional.ofNullable(seatDtoList.get(i)).orElseThrow(() -> new CookFrameException(BaseCode.SEAT_NOT_EXIST));
            orderTicketUserCreateDto.setSeatId(seatDto.getId());
            orderTicketUserCreateDto.setOrderPrice(seatDto.getPrice());
            orderTicketUserCreateDto.setCreateOrderTime(DateUtils.now());
            orderTicketUserCreateDtoList.add(orderTicketUserCreateDto);
        }
        
        orderCreateDto.setOrderTicketUserCreateDtoList(orderTicketUserCreateDtoList);
        
    }
}
