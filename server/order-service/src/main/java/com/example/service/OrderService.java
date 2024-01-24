package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.core.RedisKeyEnum;
import com.example.dto.OrderCancelDto;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderTicketUserCreateDto;
import com.example.entity.Order;
import com.example.entity.OrderTicketUser;
import com.example.enums.BaseCode;
import com.example.enums.OrderStatus;
import com.example.enums.SellStatus;
import com.example.exception.CookFrameException;
import com.example.mapper.OrderMapper;
import com.example.mapper.OrderTicketUserMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.DateUtils;
import com.example.vo.SeatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.core.DistributedLockConstants.ORDER_CANCEL_LOCK;

/**
 * <p>
 * 节目类型表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-12
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;
    
    @Autowired
    private OrderTicketUserService orderTicketUserService;
    
    @Autowired
    private ProgramCacheReverseOperate programCacheReverseOperate;
    
    @Autowired
    private RedisCache redisCache;
    
    @Transactional
    public String create(final OrderCreateDto orderCreateDto) {
        Order oldOrder = orderMapper.selectById(orderCreateDto.getId());
        if (Objects.nonNull(oldOrder)) {
            throw new CookFrameException(BaseCode.ORDER_EXIST);
        }
        
        Order order = new Order();
        BeanUtil.copyProperties(orderCreateDto,order);
        
        List<OrderTicketUser> orderTicketUserList = new ArrayList<>();
        for (OrderTicketUserCreateDto orderTicketUserCreateDto : orderCreateDto.getOrderTicketUserCreateDtoList()) {
            OrderTicketUser orderTicketUser = new OrderTicketUser();
            BeanUtil.copyProperties(orderTicketUserCreateDto,orderTicketUser);
            orderTicketUser.setId(uidGenerator.getUID());
            orderTicketUserList.add(orderTicketUser);
        }
        orderMapper.insert(order);
        orderTicketUserService.saveBatch(orderTicketUserList);
        return String.valueOf(order.getId());
    }
    
    @Transactional
    @ServiceLock(name = ORDER_CANCEL_LOCK,keys = {"#orderCancelDto.orderId"})
    public boolean cancel(OrderCancelDto orderCancelDto){
        Order order = orderMapper.selectById(orderCancelDto.getOrderId());
        if (Objects.isNull(order)) {
            throw new CookFrameException(BaseCode.ORDER_NOT_EXIST);
        }
        if (Objects.equals(order.getOrderStatus(), OrderStatus.CANCEL.getCode())) {
            throw new CookFrameException(BaseCode.ORDER_CANCEL);
        }
        if (Objects.equals(order.getOrderStatus(), OrderStatus.PAY.getCode())) {
            throw new CookFrameException(BaseCode.ORDER_PAY);
        }
        if (Objects.equals(order.getOrderStatus(), OrderStatus.REFUND.getCode())) {
            throw new CookFrameException(BaseCode.ORDER_REFUND);
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStatus(OrderStatus.CANCEL.getCode());
        updateOrder.setCancelOrderTime(DateUtils.now());
        int updateOrderResult = orderMapper.updateById(updateOrder);
        
        OrderTicketUser updateOrderTicketUser = new OrderTicketUser();
        updateOrderTicketUser.setOrderStatus(OrderStatus.CANCEL.getCode());
        updateOrderTicketUser.setCancelOrderTime(DateUtils.now());
        
        LambdaUpdateWrapper<OrderTicketUser> orderTicketUserLambdaUpdateWrapper = 
                Wrappers.lambdaUpdate(OrderTicketUser.class).eq(OrderTicketUser::getOrderId, order.getId());
        
        int updateTicketUserOrderResult = 
                orderTicketUserMapper.update(updateOrderTicketUser,orderTicketUserLambdaUpdateWrapper);
        if (updateOrderResult <= 0 || updateTicketUserOrderResult <= 0) {
            throw new CookFrameException(BaseCode.ORDER_CANAL_ERROR); 
        }
        
        LambdaQueryWrapper<OrderTicketUser> orderTicketUserLambdaQueryWrapper =
                Wrappers.lambdaQuery(OrderTicketUser.class).eq(OrderTicketUser::getOrderId, orderCancelDto.getOrderId());
        List<OrderTicketUser> orderTicketUserList = orderTicketUserMapper.selectList(orderTicketUserLambdaQueryWrapper);
        if (CollectionUtil.isEmpty(orderTicketUserList)) {
            throw new CookFrameException(BaseCode.TICKET_USER_ORDER_NOT_EXIST);
        }
        Long programId = orderTicketUserList.get(0).getProgramId();
        
        List<String> seatIdList =
                orderTicketUserList.stream().map(OrderTicketUser::getSeatId).map(String::valueOf).collect(Collectors.toList());
        List<SeatVo> seatVoList = redisCache.multiGetForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId), seatIdList, SeatVo.class);
        if (CollectionUtil.isEmpty(seatVoList)) {
            throw new CookFrameException(BaseCode.LOCK_SEAT_LIST_EMPTY);
        }
        
        //redis恢复库存
        Map<Long, Long> increaseMap = seatVoList.stream().collect(Collectors.groupingBy(SeatVo::getTicketCategoryId, Collectors.counting()));
        JSONArray jsonArray = new JSONArray();
        increaseMap.forEach((k,v) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ticketCategoryId",String.valueOf(k));
            jsonObject.put("increaseCount",v);
            jsonArray.add(jsonObject);
        });
        //redis解除锁座位
        List<String> unLockSeatIdList = seatVoList.stream().map(SeatVo::getId).map(String::valueOf).collect(Collectors.toList());
        Map<String, SeatVo> unLockSeatVoMap = seatVoList.stream().collect(Collectors
                .toMap(seatVo -> String.valueOf(seatVo.getId()), seatVo -> seatVo, (v1, v2) -> v2));
        List<String> seatNoSoldDataList = new ArrayList<>();
        unLockSeatVoMap.forEach((k,v) -> {
            seatNoSoldDataList.add(k);
            v.setSellStatus(SellStatus.NO_SOLD.getCode());
            seatNoSoldDataList.add(JSON.toJSONString(v));
        });
        List<String> keys = new ArrayList<>();
        //恢复库存的key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId).getRelKey());
        //锁定座位的key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
        //没有售卖座位的key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
        
        String[] data = new String[3];
        //恢复库存数据
        data[0] = JSON.toJSONString(jsonArray);
        //扣除锁定的座位数据
        data[1] = JSON.toJSONString(unLockSeatIdList);
        //添加未售卖的座位数据
        data[2] = JSON.toJSONString(seatNoSoldDataList);
        
        programCacheReverseOperate.programCacheReverseOperate(keys,data);
        
        return true;
    }
}
