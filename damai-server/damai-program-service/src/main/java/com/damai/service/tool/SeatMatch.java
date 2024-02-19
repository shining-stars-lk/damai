package com.damai.service.tool;

import com.damai.vo.SeatVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 座位自动匹配
 * @author: 阿宽不是程序员
 **/
public class SeatMatch {
    
    public static List<SeatVo> findAdjacentSeatVos(List<SeatVo> allSeats, int seatCount) {
        List<SeatVo> adjacentSeats = new ArrayList<>();
        
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
                SeatVo current = allSeats.get(i + j);
                SeatVo next = allSeats.get(i + j + 1);
                
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
