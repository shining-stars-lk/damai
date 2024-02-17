package com.damai.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.client.OrderClient;
import com.damai.common.ApiResponse;
import com.damai.composite.CompositeContainer;
import com.damai.core.RedisKeyEnum;
import com.damai.dto.DelayOrderCancelDto;
import com.damai.dto.OrderCreateDto;
import com.damai.dto.OrderTicketUserCreateDto;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.dto.SeatDto;
import com.damai.entity.ProgramShowTime;
import com.damai.enums.BaseCode;
import com.damai.enums.CompositeCheckType;
import com.damai.enums.OrderStatus;
import com.damai.enums.SellStatus;
import com.damai.exception.DaMaiFrameException;
import com.damai.mapper.ProgramMapper;
import com.damai.mapper.SeatMapper;
import com.damai.mapper.TicketCategoryMapper;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyWrap;
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
    private CompositeContainer compositeContainer;
    
    @Autowired
    private DelayOrderCancelSend delayOrderCancelSend;
    
    
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
                Long remainNumber = Optional.ofNullable(ticketCategoryRemainNumber.get(String.valueOf(ticketCategoryId))).orElseThrow(() -> new DaMaiFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST_V2));
                if (purchaseCount > remainNumber) {
                    throw new DaMaiFrameException(BaseCode.TICKET_REMAIN_NUMBER_NOT_SUFFICIENT);
                }
            }
            //循环入参的座位对象
            for (SeatDto seatDto : seatDtoList) {
                //验证入参的对象在库中的状态，不存在、已锁、已售卖
                Map<String, SeatVo> seatVoMap = seatVoList.stream().collect(Collectors
                        .toMap(seat -> seat.getRowCode() + "-" + seat.getColCode(), seat -> seat, (v1, v2) -> v2));
                SeatVo seatVo = seatVoMap.get(seatDto.getRowCode() + "-" + seatDto.getColCode());
                if (Objects.isNull(seatVo)) {
                    throw new DaMaiFrameException(BaseCode.SEAT_NOT_EXIST);
                }
                if (Objects.equals(seatVo.getSellStatus(), SellStatus.LOCK.getCode())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_LOCK);
                }
                if (Objects.equals(seatVo.getSellStatus(), SellStatus.SOLD.getCode())) {
                    throw new DaMaiFrameException(BaseCode.SEAT_SOLD);
                }
                purchaseSeatList.add(seatVo);
                //将入参的座位价格进行累加
                parameterOrderPrice = parameterOrderPrice.add(seatDto.getPrice());
                //将库中的座位价格进行类型
                databaseOrderPrice = databaseOrderPrice.add(seatVo.getPrice());
            }
            if (parameterOrderPrice.compareTo(databaseOrderPrice) > 0) {
                throw new DaMaiFrameException(BaseCode.PRICE_ERROR);
            }
        }else {
            //入参座位不存在，利用算法自动根据人数和票档进行分配相邻座位
            Long ticketCategoryId = programOrderCreateDto.getTicketCategoryId();
            Integer ticketCount = programOrderCreateDto.getTicketCount();
            //余票检测
            Long remainNumber = Optional.ofNullable(ticketCategoryRemainNumber.get(String.valueOf(ticketCategoryId))).orElseThrow(() -> new DaMaiFrameException(BaseCode.TICKET_CATEGORY_NOT_EXIST_V2));
            if (ticketCount > remainNumber) {
                throw new DaMaiFrameException(BaseCode.TICKET_REMAIN_NUMBER_NOT_SUFFICIENT);
            }
            purchaseSeatList = SeatMatch.findAdjacentSeatVos(seatVoList.stream().filter(seatVo -> 
                    Objects.equals(seatVo.getTicketCategoryId(), ticketCategoryId)).collect(Collectors.toList()), ticketCount);
            //如果匹配出来的座位数量小于要购买的数量，拒绝执行
            if (purchaseSeatList.size() < ticketCount) {
                throw new DaMaiFrameException(BaseCode.SEAT_OCCUPY);
            }
        }
        //进行操作缓存中的数据
        updateProgramCacheData(programId,purchaseSeatList,OrderStatus.NO_PAY);
        //将筛选出来的购买的座位信息传入，执行创建订单的操作
        return doCreate(programOrderCreateDto,purchaseSeatList);
    }
    
    
    public String createNew(ProgramOrderCreateDto programOrderCreateDto) {
        compositeContainer.execute(CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue(),programOrderCreateDto);
        //节目id
        Long programId = programOrderCreateDto.getProgramId();
        List<SeatDto> seatDtoList = programOrderCreateDto.getSeatDtoList();
        List<String> keys = new ArrayList<>();
        //票档数量的key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId).getRelKey());
        //没有售卖的座位key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
        //锁定的座位key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
        String[] data = new String[2];
        //入参座位存在
        JSONArray jsonArray = new JSONArray();
        if (CollectionUtil.isNotEmpty(seatDtoList)) {
            keys.add("1");
            Map<Long, Long> seatTicketCategoryDtoCount = seatDtoList.stream()
                    .collect(Collectors.groupingBy(SeatDto::getTicketCategoryId, Collectors.counting()));
            //将入参的座位集合统计出票档id和票档数量
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
            //如果不选择座位，则直接传入票档id和票档数量
            Long ticketCategoryId = programOrderCreateDto.getTicketCategoryId();
            Integer ticketCount = programOrderCreateDto.getTicketCount();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ticketCategoryId",ticketCategoryId);
            jsonObject.put("ticketCount",ticketCount);
            jsonArray.add(jsonObject);
        }
        data[0] = JSON.toJSONString(jsonArray);
        data[1] = JSON.toJSONString(seatDtoList);
        //升级后的lua脚本处理票档数量和座位状态的检验，以及扣减票档数量的操作
        String result = programCacheCreateOrderOperate.programCacheOperate(keys, data);
        ProgramCacheCreateOrderData programCacheCreateOrderData = JSON.parseObject(result, ProgramCacheCreateOrderData.class);
        if (!Objects.equals(programCacheCreateOrderData.getCode(), BaseCode.SUCCESS.getCode())) {
            throw new DaMaiFrameException(Objects.requireNonNull(BaseCode.getRc(programCacheCreateOrderData.getCode())));
        }
        List<SeatVo> purchaseSeatList = programCacheCreateOrderData.getPurchaseSeatList();
        //将筛选出来的购买的座位信息传入，执行创建订单的操作
        return doCreate(programOrderCreateDto,purchaseSeatList);
    }
    
    private String doCreate(ProgramOrderCreateDto programOrderCreateDto,List<SeatVo> purchaseSeatList){
        Long programId = programOrderCreateDto.getProgramId();
        //获取要购买的节目信息
        ProgramVo programVo = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM, programId), ProgramVo.class);
        //查询节目演出时间
        ProgramShowTime programShowTime = redisCache.get(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SHOW_TIME
                ,programId),ProgramShowTime.class);
        //主订单参数构建
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        //生成订单编号
        orderCreateDto.setOrderNumber(uidGenerator.getOrderNumber(programOrderCreateDto.getUserId(),ORDER_TABLE_COUNT));
        orderCreateDto.setProgramId(programOrderCreateDto.getProgramId());
        orderCreateDto.setUserId(programOrderCreateDto.getUserId());
        orderCreateDto.setProgramTitle(programVo.getTitle());
        orderCreateDto.setProgramPlace(programVo.getPlace());
        orderCreateDto.setProgramShowTime(programShowTime.getShowTime());
        BigDecimal databaseOrderPrice = purchaseSeatList.stream().map(SeatVo::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        orderCreateDto.setOrderPrice(databaseOrderPrice);
        orderCreateDto.setCreateOrderTime(DateUtils.now());
        
        //购票人订单构建
        List<Long> ticketUserIdList = programOrderCreateDto.getTicketUserIdList();
        List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList = new ArrayList<>();
        for (int i = 0; i < ticketUserIdList.size(); i++) {
            Long ticketUserId = ticketUserIdList.get(i);
            OrderTicketUserCreateDto orderTicketUserCreateDto = new OrderTicketUserCreateDto();
            orderTicketUserCreateDto.setOrderNumber(orderCreateDto.getOrderNumber());
            orderTicketUserCreateDto.setProgramId(programOrderCreateDto.getProgramId());
            orderTicketUserCreateDto.setUserId(programOrderCreateDto.getUserId());
            orderTicketUserCreateDto.setTicketUserId(ticketUserId);
            SeatVo seatVo = Optional.ofNullable(purchaseSeatList.get(i)).orElseThrow(() -> new DaMaiFrameException(BaseCode.SEAT_NOT_EXIST));
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
            //订单创建失败将操作缓存中的数据还原
            updateProgramCacheData(programId,purchaseSeatList,OrderStatus.CANCEL);
            log.error("创建订单失败 需人工处理 orderCreateDto : {}",JSON.toJSONString(orderCreateDto));
            throw new DaMaiFrameException(createOrderResponse);
        }
        
        //延迟队列创建
        DelayOrderCancelDto delayOrderCancelDto = new DelayOrderCancelDto();
        delayOrderCancelDto.setOrderNumber(orderCreateDto.getOrderNumber());
        delayOrderCancelSend.sendMessage(JSON.toJSONString(delayOrderCancelDto));
        
        return orderNumber;
    }
    
    private void updateProgramCacheData(long programId,List<SeatVo> seatVoList,OrderStatus orderStatus){
        if (!(Objects.equals(orderStatus.getCode(), OrderStatus.NO_PAY.getCode()) ||
                Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode()))) {
            throw new DaMaiFrameException(BaseCode.OPERATE_ORDER_STATUS_NOT_PERMIT);
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
}
