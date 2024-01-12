package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Seat;
import com.example.enums.SellStatus;
import com.example.mapper.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 座位表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-11
 */
@Service
public class SeatService extends ServiceImpl<SeatMapper, Seat> {
    
    @Autowired
    private SeatMapper seatMapper;
    
    public List<Seat> test(final Integer count) {
        LambdaQueryWrapper<Seat> seatLambdaQueryWrapper = Wrappers.lambdaQuery(Seat.class)
                .eq(Seat::getProgramId, 1)
                .eq(Seat::getSellStatus, SellStatus.NO_SOLD.getCode());
        List<Seat> seats = seatMapper.selectList(seatLambdaQueryWrapper);
        return findAdjacentSeats(seats, count);
    }
    
    public List<Seat> findAdjacentSeats(List<Seat> allSeats, int seatCount) {
        List<Seat> adjacentSeats = new ArrayList<>();
        
        // 对可用座位排序
        allSeats.sort((s1, s2) -> {
            if (Objects.equals(s1.getRowCode(), s2.getRowCode())) {
                return s1.getColCode() - s2.getColCode();
            } else {
                return s1.getRowCode() - s2.getRowCode();
            }
        });
        
        // 寻找相邻座位
        for (int i = 0; i < allSeats.size() - seatCount + 1; i++) {
            boolean seatsFound = true;
            for (int j = 0; j < seatCount - 1; j++) {
                Seat current = allSeats.get(i + j);
                Seat next = allSeats.get(i + j + 1);
                
                if (!(Objects.equals(current.getRowCode(), next.getRowCode()) && next.getColCode() - current.getColCode() == 1)) {
                    seatsFound = false;
                    break;
                }
            }
            if (seatsFound) {
                for (int k = 0; k < seatCount; k++) {
                    adjacentSeats.add(allSeats.get(i + k));
                }
                return adjacentSeats;
            }
        }
        // 如果没有找到，返回空列表
        return adjacentSeats; 
    }
}
