package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.example.client.OrderClient;
import com.example.client.UserClient;
import com.example.common.ApiResponse;
import com.example.composite.CompositeContainer;
import com.example.core.RedisKeyEnum;
import com.example.dto.DelayOrderCancelDto;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderTicketUserCreateDto;
import com.example.dto.ProgramOrderCreateDto;
import com.example.dto.SeatDto;
import com.example.entity.ProgramShowTime;
import com.example.enums.BaseCode;
import com.example.enums.CompositeCheckType;
import com.example.enums.OrderStatus;
import com.example.enums.SellStatus;
import com.example.exception.CookFrameException;
import com.example.mapper.ProgramMapper;
import com.example.mapper.SeatMapper;
import com.example.mapper.TicketCategoryMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.service.delaysend.DelayOrderCancelSend;
import com.example.service.tool.SeatMatch;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.DateUtils;
import com.example.vo.ProgramVo;
import com.example.vo.SeatVo;
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

import static com.example.core.DistributedLockConstants.PROGRAM_ORDER_CREATE;
import static com.example.service.constant.ProgramOrderConstant.ORDER_TABLE_COUNT;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-11
 **/
@Slf4j
@Service
public class ProgramOrderService {
    
    @Autowired
    private ProgramMapper programMapper;
    
    @Autowired
    private SeatMapper seatMapper;
    
    @Autowired
    private TicketCategoryMapper ticketCategoryMapper;
    
    @Autowired
    private UserClient userClient;
    
    @Autowired
    private OrderClient orderClient;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ProgramCacheOperate programCacheOperate;
    
    @Autowired
    private ProgramCacheOperateV2 programCacheOperateV2;
    
    @Autowired
    private CompositeContainer compositeContainer;
    
    @Autowired
    private DelayOrderCancelSend delayOrderCancelSend;
    
    /**
     * 订单创建，使用节目id作为锁
     * */
    @ServiceLock(name = PROGRAM_ORDER_CREATE,keys = {"#programOrderCreateDto.programId"})
    public String create(ProgramOrderCreateDto programOrderCreateDto) {
        compositeContainer.execute(CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue(),programOrderCreateDto);
        //节目id
        Long programId = programOrderCreateDto.getProgramId();
        //传入的座位总价格
        BigDecimal parameterOrderPrice = new BigDecimal("0");
        //库中的座位总价格
        BigDecimal databaseOrderPrice = new BigDecimal("0");
        //要购买的座位
        List<SeatVo> purchaseSeatList = new ArrayList<>();
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<Long> ticketUserIdList = programOrderCreateDto.getTicketUserIdList();
        //该节目下所有未售卖的座位
        List<SeatVo> seatVoList = redisCache.getAllForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programOrderCreateDto.getProgramId()), SeatVo.class);
        //该节目下的余票数量
        Map<String, Long> ticketCategoryRemainNumber = redisCache.getAllMapForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programOrderCreateDto.getProgramId()), Long.class);
        //入参座位存在
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            //余票数量检测
            Map<Long, Long> seatTicketCategoryDtoCount = seatDtoList.stream()
                    .collect(Collectors.groupingBy(SeatDto::getTicketCategoryId, Collectors.counting()));
            for (final Entry<Long, Long> entry : seatTicketCategoryDtoCount.entrySet()) {
                Long ticketCategoryId = entry.getKey();
                Long purchaseCount = entry.getValue();
                Long remainNumber = Optional.ofNullable(ticketCategoryRemainNumber.get(String.valueOf(ticketCategoryId))).orElseThrow(() -> new CookFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST_V2));
                if (purchaseCount > remainNumber) {
                    throw new CookFrameException(BaseCode.TICKET_REMAIN_NUMBER_NOT_SUFFICIENT);
                }
            }
            //循环入参的座位对象
            for (SeatDto seatDto : seatDtoList) {
                //验证入参的对象在库中的状态，不存在、已锁、已售卖
                Map<String, SeatVo> seatVoMap = seatVoList.stream().collect(Collectors
                        .toMap(seat -> seat.getRowCode() + "-" + seat.getColCode(), seat -> seat, (v1, v2) -> v2));
                SeatVo seatVo = seatVoMap.get(seatDto.getRowCode() + "-" + seatDto.getColCode());
                if (Objects.isNull(seatVo)) {
                    throw new CookFrameException(BaseCode.SEAT_NOT_EXIST);
                }
                if (Objects.equals(seatVo.getSellStatus(), SellStatus.LOCK.getCode())) {
                    throw new CookFrameException(BaseCode.SEAT_LOCK);
                }
                if (Objects.equals(seatVo.getSellStatus(), SellStatus.SOLD.getCode())) {
                    throw new CookFrameException(BaseCode.SEAT_SOLD);
                }
                purchaseSeatList.add(seatVo);
                //将入参的座位价格进行累加
                parameterOrderPrice = parameterOrderPrice.add(seatDto.getPrice());
                //将库中的座位价格进行类型
                databaseOrderPrice = databaseOrderPrice.add(seatVo.getPrice());
            }
            if (parameterOrderPrice.compareTo(databaseOrderPrice) > 0) {
                throw new CookFrameException(BaseCode.PRICE_ERROR);
            }
        }else {
            //入参座位不存在，利用算法自动根据人数和票档进行分配相邻座位
            Long ticketCategoryId = programOrderCreateDto.getTicketCategoryId();
            Integer ticketCount = programOrderCreateDto.getTicketCount();
            //余票检测
            Long remainNumber = Optional.ofNullable(ticketCategoryRemainNumber.get(String.valueOf(ticketCategoryId))).orElseThrow(() -> new CookFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST_V2));
            if (ticketCount > remainNumber) {
                throw new CookFrameException(BaseCode.TICKET_REMAIN_NUMBER_NOT_SUFFICIENT);
            }
            purchaseSeatList = SeatMatch.findAdjacentSeatVos(seatVoList.stream().filter(seatVo -> 
                    Objects.equals(seatVo.getTicketCategoryId(), ticketCategoryId)).collect(Collectors.toList()), ticketCount);
            //如果匹配出来的座位数量小于要购买的数量，拒绝执行
            if (purchaseSeatList.size() < ticketCount) {
                throw new CookFrameException(BaseCode.SEAT_OCCUPY);
            }
        }
        //进行操作缓存中的数据
        updateProgramCacheData(programId,purchaseSeatList,OrderStatus.NO_PAY);
        
        //获取要购买的节目信息
        ProgramVo programVo = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM, programOrderCreateDto.getProgramId()), ProgramVo.class);
        //查询节目演出时间
        ProgramShowTime programShowTime = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SHOW_TIME
                ,programOrderCreateDto.getProgramId()),ProgramShowTime.class);
        //主订单参数构建
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        //生成订单编号
        orderCreateDto.setOrderNumber(uidGenerator.getOrderNumber(programOrderCreateDto.getUserId(),ORDER_TABLE_COUNT));
        orderCreateDto.setProgramId(programOrderCreateDto.getProgramId());
        orderCreateDto.setUserId(programOrderCreateDto.getUserId());
        orderCreateDto.setProgramTitle(programVo.getTitle());
        orderCreateDto.setProgramPlace(programVo.getPlace());
        orderCreateDto.setProgramShowTime(programShowTime.getShowTime());
        orderCreateDto.setOrderPrice(parameterOrderPrice);
        orderCreateDto.setCreateOrderTime(DateUtils.now());
        
        //购票人订单构建
        List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList = new ArrayList<>();
        for (int i = 0; i < ticketUserIdList.size(); i++) {
            Long ticketUserId = ticketUserIdList.get(i);
            OrderTicketUserCreateDto orderTicketUserCreateDto = new OrderTicketUserCreateDto();
            orderTicketUserCreateDto.setOrderNumber(orderCreateDto.getOrderNumber());
            orderTicketUserCreateDto.setProgramId(programOrderCreateDto.getProgramId());
            orderTicketUserCreateDto.setUserId(programOrderCreateDto.getUserId());
            orderTicketUserCreateDto.setTicketUserId(ticketUserId);
            SeatVo seatVo = Optional.ofNullable(purchaseSeatList.get(i)).orElseThrow(() -> new CookFrameException(BaseCode.SEAT_NOT_EXIST));
            orderTicketUserCreateDto.setSeatId(seatVo.getId());
            orderTicketUserCreateDto.setSeatInfo(seatVo.getRowCode()+"排"+seatVo.getColCode()+"列");
            orderTicketUserCreateDto.setOrderPrice(seatVo.getPrice());
            orderTicketUserCreateDto.setCreateOrderTime(DateUtils.now());
            orderTicketUserCreateDtoList.add(orderTicketUserCreateDto);
        }
        
        orderCreateDto.setOrderTicketUserCreateDtoList(orderTicketUserCreateDtoList);
        
        String orderId;
        ApiResponse<String> createOrderResponse = orderClient.create(orderCreateDto);
        if (Objects.equals(createOrderResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            orderId = createOrderResponse.getData();
        }else {
            //订单创建失败将操作缓存中的数据还原
            updateProgramCacheData(programId,purchaseSeatList,OrderStatus.CANCEL);
            log.error("创建订单失败 需人工处理 orderCreateDto : {}",JSON.toJSONString(orderCreateDto));
            throw new CookFrameException(createOrderResponse);
        }
        
        //延迟队列创建
        DelayOrderCancelDto delayOrderCancelDto = new DelayOrderCancelDto();
        delayOrderCancelDto.setOrderNumber(orderCreateDto.getOrderNumber());
        delayOrderCancelSend.sendMessage(JSON.toJSONString(delayOrderCancelDto));
        
        return orderId;
    }
    
    private void updateProgramCacheData(long programId,List<SeatVo> seatVoList,OrderStatus orderStatus){
        if (!(Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode()) ||
                Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode()))) {
            throw new CookFrameException(BaseCode.OPERATE_ORDER_STATUS_NOT_PERMIT);
        }
        //座位id集合
        List<String> seatIdList = seatVoList.stream().map(SeatVo::getId).map(String::valueOf).collect(Collectors.toList());
        //座位map
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
        //票档数量的key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId).getRelKey());
        
        String[] data = new String[3];
        //根据座位集合统计出对应的票档数量
        Map<Long, Long> ticketCategoryCountMap = seatVoList.stream().collect(Collectors.groupingBy(SeatVo::getTicketCategoryId, Collectors.counting()));
        //要扣除或者恢复的票档数量
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
        //票档数量数据
        data[0] = JSON.toJSONString(jsonArray);
        
        //如果是订单创建，那么就扣除未售卖的座位id
        //如果是订单取消，那么就扣除锁定的座位id
        data[1] = JSON.toJSONString(seatIdList);
        //如果是订单创建的操作，那么添加到锁定的座位数据
        //如果是订单订单的操作，那么添加到未售卖的座位数据
        data[2] = JSON.toJSONString(seatDataList);
        if (Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode())) {
            //没有售卖座位的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
            //锁定座位的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
        } else if (Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode())) {
            //锁定座位的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
            //没有售卖座位的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
        }
        
        programCacheOperate.programCacheOperate(keys,data);
    }
    
    
    /**
     * 订单创建，进行优化，使用节目id作为锁
     * */
    public String createNew(ProgramOrderCreateDto programOrderCreateDto) {
        compositeContainer.execute(CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue(),programOrderCreateDto);
        //节目id
        Long programId = programOrderCreateDto.getProgramId();
        //传入的座位总价格
        BigDecimal parameterOrderPrice = new BigDecimal("0");
        //库中的座位总价格
        BigDecimal databaseOrderPrice = new BigDecimal("0");
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<String> keys = new ArrayList<>();
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programOrderCreateDto.getProgramId()).getRelKey());
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programOrderCreateDto.getProgramId()).getRelKey());
        String[] data = new String[2];
        //入参座位存在
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            keys.add("1");
            Map<Long, Long> seatTicketCategoryDtoCount = seatDtoList.stream()
                    .collect(Collectors.groupingBy(SeatDto::getTicketCategoryId, Collectors.counting()));
            for (final Entry<Long, Long> entry : seatTicketCategoryDtoCount.entrySet()) {
                Long ticketCategoryId = entry.getKey();
                Long ticketCount = entry.getValue();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ticketCategoryId",ticketCategoryId);
                jsonObject.put("ticketCount",ticketCount);
                jsonArray.add(jsonObject);
            }
        }else {
            //入参座位不存在
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
        //TODO lua脚本
        String s = programCacheOperateV2.programCacheOperate(keys, data);
        System.out.println(s);
        
        
        return null;
        
        
        
        
//        //进行操作缓存中的数据
//        updateProgramCacheData(programId,purchaseSeatList,OrderStatus.NO_PAY);
//        
//        //获取要购买的节目信息
//        ProgramVo programVo = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM, programOrderCreateDto.getProgramId()), ProgramVo.class);
//        //查询节目演出时间
//        ProgramShowTime programShowTime = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SHOW_TIME
//                ,programOrderCreateDto.getProgramId()),ProgramShowTime.class);
//        //主订单参数构建
//        OrderCreateDto orderCreateDto = new OrderCreateDto();
//        //生成订单编号
//        orderCreateDto.setOrderNumber(uidGenerator.getOrderNumber(programOrderCreateDto.getUserId(),ORDER_TABLE_COUNT));
//        orderCreateDto.setProgramId(programOrderCreateDto.getProgramId());
//        orderCreateDto.setUserId(programOrderCreateDto.getUserId());
//        orderCreateDto.setProgramTitle(programVo.getTitle());
//        orderCreateDto.setProgramPlace(programVo.getPlace());
//        orderCreateDto.setProgramShowTime(programShowTime.getShowTime());
//        orderCreateDto.setOrderPrice(parameterOrderPrice);
//        orderCreateDto.setCreateOrderTime(DateUtils.now());
//        
//        //购票人订单构建
//        List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList = new ArrayList<>();
//        for (int i = 0; i < ticketUserIdList.size(); i++) {
//            Long ticketUserId = ticketUserIdList.get(i);
//            OrderTicketUserCreateDto orderTicketUserCreateDto = new OrderTicketUserCreateDto();
//            orderTicketUserCreateDto.setOrderNumber(orderCreateDto.getOrderNumber());
//            orderTicketUserCreateDto.setProgramId(programOrderCreateDto.getProgramId());
//            orderTicketUserCreateDto.setUserId(programOrderCreateDto.getUserId());
//            orderTicketUserCreateDto.setTicketUserId(ticketUserId);
//            SeatVo seatVo = Optional.ofNullable(purchaseSeatList.get(i)).orElseThrow(() -> new CookFrameException(BaseCode.SEAT_NOT_EXIST));
//            orderTicketUserCreateDto.setSeatId(seatVo.getId());
//            orderTicketUserCreateDto.setSeatInfo(seatVo.getRowCode()+"排"+seatVo.getColCode()+"列");
//            orderTicketUserCreateDto.setOrderPrice(seatVo.getPrice());
//            orderTicketUserCreateDto.setCreateOrderTime(DateUtils.now());
//            orderTicketUserCreateDtoList.add(orderTicketUserCreateDto);
//        }
//        
//        orderCreateDto.setOrderTicketUserCreateDtoList(orderTicketUserCreateDtoList);
//        
//        String orderId;
//        ApiResponse<String> createOrderResponse = orderClient.create(orderCreateDto);
//        if (Objects.equals(createOrderResponse.getCode(), BaseCode.SUCCESS.getCode())) {
//            orderId = createOrderResponse.getData();
//        }else {
//            //订单创建失败将操作缓存中的数据还原
//            updateProgramCacheData(programId,purchaseSeatList,OrderStatus.CANCEL);
//            log.error("创建订单失败 需人工处理 orderCreateDto : {}",JSON.toJSONString(orderCreateDto));
//            throw new CookFrameException(createOrderResponse);
//        }
//        
//        //延迟队列创建
//        DelayOrderCancelDto delayOrderCancelDto = new DelayOrderCancelDto();
//        delayOrderCancelDto.setOrderNumber(orderCreateDto.getOrderNumber());
//        delayOrderCancelSend.sendMessage(JSON.toJSONString(delayOrderCancelDto));
//        
//        return orderId;
    }
}
