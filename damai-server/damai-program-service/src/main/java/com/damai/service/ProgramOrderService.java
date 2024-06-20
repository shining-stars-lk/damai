package com.damai.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.client.OrderClient;
import com.damai.common.ApiResponse;
import com.damai.core.RedisKeyManage;
import com.damai.dto.DelayOrderCancelDto;
import com.damai.dto.OrderCreateDto;
import com.damai.dto.OrderTicketUserCreateDto;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.dto.SeatDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.enums.OrderStatus;
import com.damai.enums.SellStatus;
import com.damai.exception.DaMaiFrameException;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.delaysend.DelayOrderCancelSend;
import com.damai.service.lua.ProgramCacheCreateOrderData;
import com.damai.service.lua.ProgramCacheCreateOrderOperate;
import com.damai.service.lua.ProgramCacheOperate;
import com.damai.service.tool.SeatMatch;
import com.damai.util.DateUtils;
import com.damai.vo.ProgramVo;
import com.damai.vo.SeatVo;
import lombok.extern.slf4j.Slf4j;
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

import static com.damai.service.constant.ProgramOrderConstant.ORDER_TABLE_COUNT;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目订单 service
 * @author: 阿星不是程序员
 **/
@Slf4j
@Service
public class ProgramOrderService {
    
    @Autowired
    private OrderClient orderClient;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramCacheOperate programCacheOperate;
    
    @Autowired
    private ProgramCacheCreateOrderOperate programCacheCreateOrderOperate;
    
    @Autowired
    private DelayOrderCancelSend delayOrderCancelSend;
    
    
    public String create(ProgramOrderCreateDto programOrderCreateDto) {
        Long programId = programOrderCreateDto.getProgramId();
        BigDecimal parameterOrderPrice = new BigDecimal("0");
        BigDecimal databaseOrderPrice = new BigDecimal("0");
        List<SeatVo> purchaseSeatList = new ArrayList<>();
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<SeatVo> seatVoList = 
                redisCache.getAllForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_NO_SOLD_HASH, 
                        programOrderCreateDto.getProgramId()), SeatVo.class);
        Map<String, Long> ticketCategoryRemainNumber = 
                redisCache.getAllMapForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_TICKET_REMAIN_NUMBER_HASH, 
                        programOrderCreateDto.getProgramId()), Long.class);
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            Map<Long, Long> seatTicketCategoryDtoCount = seatDtoList.stream()
                    .collect(Collectors.groupingBy(SeatDto::getTicketCategoryId, Collectors.counting()));
            for (Entry<Long, Long> entry : seatTicketCategoryDtoCount.entrySet()) {
                Long ticketCategoryId = entry.getKey();
                Long purchaseCount = entry.getValue();
                Long remainNumber = Optional.ofNullable(ticketCategoryRemainNumber.get(String.valueOf(ticketCategoryId)))
                        .orElseThrow(() -> new DaMaiFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST_V2));
                if (purchaseCount > remainNumber) {
                    throw new DaMaiFrameException(BaseCode.TICKET_REMAIN_NUMBER_NOT_SUFFICIENT);
                }
            }
            for (SeatDto seatDto : seatDtoList) {
                Map<String, SeatVo> seatVoMap = seatVoList.stream().collect(Collectors
                        .toMap(seat -> seat.getRowCode() + "-" + seat.getColCode(), seat -> seat, (v1, v2) -> v2));
                SeatVo seatVo = seatVoMap.get(seatDto.getRowCode() + "-" + seatDto.getColCode());
                if (Objects.isNull(seatVo)) {
                    throw new DaMaiFrameException(BaseCode.SEAT_IS_NOT_NOT_SOLD);
                }
                purchaseSeatList.add(seatVo);
                parameterOrderPrice = parameterOrderPrice.add(seatDto.getPrice());
                databaseOrderPrice = databaseOrderPrice.add(seatVo.getPrice());
            }
            if (parameterOrderPrice.compareTo(databaseOrderPrice) > 0) {
                throw new DaMaiFrameException(BaseCode.PRICE_ERROR);
            }
        }else {
            Long ticketCategoryId = programOrderCreateDto.getTicketCategoryId();
            Integer ticketCount = programOrderCreateDto.getTicketCount();
            Long remainNumber = Optional.ofNullable(ticketCategoryRemainNumber.get(String.valueOf(ticketCategoryId)))
                    .orElseThrow(() -> new DaMaiFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST_V2));
            if (ticketCount > remainNumber) {
                throw new DaMaiFrameException(BaseCode.TICKET_REMAIN_NUMBER_NOT_SUFFICIENT);
            }
            purchaseSeatList = SeatMatch.findAdjacentSeatVos(seatVoList.stream().filter(seatVo -> 
                    Objects.equals(seatVo.getTicketCategoryId(), ticketCategoryId)).collect(Collectors.toList()), ticketCount);
            if (purchaseSeatList.size() < ticketCount) {
                throw new DaMaiFrameException(BaseCode.SEAT_OCCUPY);
            }
        }
        updateProgramCacheData(programId,purchaseSeatList,OrderStatus.NO_PAY);
        return doCreate(programOrderCreateDto,purchaseSeatList);
    }
    
    
    public String createNew(ProgramOrderCreateDto programOrderCreateDto) {
        Long programId = programOrderCreateDto.getProgramId();
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<String> keys = new ArrayList<>();
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
        String[] data = new String[2];
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            keys.add("1");
            Map<Long, Long> seatTicketCategoryDtoCount = seatDtoList.stream()
                    .collect(Collectors.groupingBy(SeatDto::getTicketCategoryId, Collectors.counting()));
            for (Entry<Long, Long> entry : seatTicketCategoryDtoCount.entrySet()) {
                Long ticketCategoryId = entry.getKey();
                Long ticketCount = entry.getValue();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ticketCategoryId",ticketCategoryId);
                jsonObject.put("ticketCount",ticketCount);
                jsonArray.add(jsonObject);
            }
        }else {
            keys.add("2");
            Long ticketCategoryId = programOrderCreateDto.getTicketCategoryId();
            Integer ticketCount = programOrderCreateDto.getTicketCount();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ticketCategoryId",ticketCategoryId);
            jsonObject.put("ticketCount",ticketCount);
            jsonArray.add(jsonObject);
        }
        data[0] = JSON.toJSONString(jsonArray);
        data[1] = JSON.toJSONString(seatDtoList);
        ProgramCacheCreateOrderData programCacheCreateOrderData = programCacheCreateOrderOperate.programCacheOperate(keys, data);
        if (!Objects.equals(programCacheCreateOrderData.getCode(), BaseCode.SUCCESS.getCode())) {
            throw new DaMaiFrameException(Objects.requireNonNull(BaseCode.getRc(programCacheCreateOrderData.getCode())));
        }
        List<SeatVo> purchaseSeatList = programCacheCreateOrderData.getPurchaseSeatList();
        return doCreate(programOrderCreateDto,purchaseSeatList);
    }
    
    private String doCreate(ProgramOrderCreateDto programOrderCreateDto,List<SeatVo> purchaseSeatList){
        Long programId = programOrderCreateDto.getProgramId();
        ProgramVo programVo = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM, programId), ProgramVo.class);
        ProgramShowTime programShowTime = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME
                ,programId),ProgramShowTime.class);
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setOrderNumber(uidGenerator.getOrderNumber(programOrderCreateDto.getUserId(),ORDER_TABLE_COUNT));
        orderCreateDto.setProgramId(programOrderCreateDto.getProgramId());
        orderCreateDto.setProgramItemPicture(programVo.getItemPicture());
        orderCreateDto.setUserId(programOrderCreateDto.getUserId());
        orderCreateDto.setProgramTitle(programVo.getTitle());
        orderCreateDto.setProgramPlace(programVo.getPlace());
        orderCreateDto.setProgramShowTime(programShowTime.getShowTime());
        orderCreateDto.setProgramPermitChooseSeat(programVo.getPermitChooseSeat());
        BigDecimal databaseOrderPrice = 
                purchaseSeatList.stream().map(SeatVo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        orderCreateDto.setOrderPrice(databaseOrderPrice);
        orderCreateDto.setCreateOrderTime(DateUtils.now());
        
        List<Long> ticketUserIdList = programOrderCreateDto.getTicketUserIdList();
        List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList = new ArrayList<>();
        for (int i = 0; i < ticketUserIdList.size(); i++) {
            Long ticketUserId = ticketUserIdList.get(i);
            OrderTicketUserCreateDto orderTicketUserCreateDto = new OrderTicketUserCreateDto();
            orderTicketUserCreateDto.setOrderNumber(orderCreateDto.getOrderNumber());
            orderTicketUserCreateDto.setProgramId(programOrderCreateDto.getProgramId());
            orderTicketUserCreateDto.setUserId(programOrderCreateDto.getUserId());
            orderTicketUserCreateDto.setTicketUserId(ticketUserId);
            SeatVo seatVo = 
                    Optional.ofNullable(purchaseSeatList.get(i))
                            .orElseThrow(() -> new DaMaiFrameException(BaseCode.SEAT_NOT_EXIST));
            orderTicketUserCreateDto.setSeatId(seatVo.getId());
            orderTicketUserCreateDto.setSeatInfo(seatVo.getRowCode()+"排"+seatVo.getColCode()+"列");
            orderTicketUserCreateDto.setOrderPrice(seatVo.getPrice());
            orderTicketUserCreateDto.setCreateOrderTime(DateUtils.now());
            orderTicketUserCreateDtoList.add(orderTicketUserCreateDto);
        }
        
        orderCreateDto.setOrderTicketUserCreateDtoList(orderTicketUserCreateDtoList);
        
        String orderNumber;
        ApiResponse<String> createOrderResponse = orderClient.create(orderCreateDto);
        if (Objects.equals(createOrderResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            orderNumber = createOrderResponse.getData();
        }else {
            updateProgramCacheData(programId,purchaseSeatList,OrderStatus.CANCEL);
            log.error("创建订单失败 需人工处理 orderCreateDto : {}",JSON.toJSONString(orderCreateDto));
            throw new DaMaiFrameException(createOrderResponse);
        }
        
        DelayOrderCancelDto delayOrderCancelDto = new DelayOrderCancelDto();
        delayOrderCancelDto.setOrderNumber(orderCreateDto.getOrderNumber());
        delayOrderCancelSend.sendMessage(JSON.toJSONString(delayOrderCancelDto));
        
        return orderNumber;
    }
    
    private void updateProgramCacheData(Long programId,List<SeatVo> seatVoList,OrderStatus orderStatus){
        if (!(Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode()) ||
                Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode()))) {
            throw new DaMaiFrameException(BaseCode.OPERATE_ORDER_STATUS_NOT_PERMIT);
        }
        List<String> seatIdList = seatVoList.stream().map(SeatVo::getId).map(String::valueOf).collect(Collectors.toList());
        Map<String, SeatVo> seatVoMap = seatVoList.stream().collect(Collectors
                .toMap(seatVo -> String.valueOf(seatVo.getId()), seatVo -> seatVo, (v1, v2) -> v2));
        List<String> seatDataList = new ArrayList<>();
        seatVoMap.forEach((k,v) -> {
            seatDataList.add(k);
            if (Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode())){
                v.setSellStatus(SellStatus.NO_SOLD.getCode());
            }else if (Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode())) {
                v.setSellStatus(SellStatus.LOCK.getCode());
            }
            seatDataList.add(JSON.toJSONString(v));
        });
        List<String> keys = new ArrayList<>();
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId).getRelKey());
        
        String[] data = new String[3];
        Map<Long, Long> ticketCategoryCountMap = 
                seatVoList.stream().collect(Collectors.groupingBy(SeatVo::getTicketCategoryId, Collectors.counting()));
        JSONArray jsonArray = new JSONArray();
        ticketCategoryCountMap.forEach((k,v) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ticketCategoryId",String.valueOf(k));
            if (Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode())) {
                jsonObject.put("count","-" + v);
            } else if (Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode())) {
                jsonObject.put("count",v);
            }
            jsonArray.add(jsonObject);
        });
        
        data[0] = JSON.toJSONString(jsonArray);
        data[1] = JSON.toJSONString(seatIdList);
        data[2] = JSON.toJSONString(seatDataList);
     
        if (Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode())) {
            keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
            keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
        } else if (Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode())) {
            keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
            keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
        }
        programCacheOperate.programCacheOperate(keys,data);
    }
}
