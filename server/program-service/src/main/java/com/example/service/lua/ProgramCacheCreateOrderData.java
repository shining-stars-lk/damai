package com.example.service.lua;

import com.example.vo.SeatVo;
import lombok.Data;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-05
 **/
@Data
public class ProgramCacheCreateOrderData {

    private Integer code;
    
    private List<SeatVo> purchaseSeatList;
}
