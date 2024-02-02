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
import com.example.client.PayClient;
import com.example.client.UserClient;
import com.example.common.ApiResponse;
import com.example.core.RedisKeyEnum;
import com.example.dto.NotifyDto;
import com.example.dto.OrderCancelDto;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderGetDto;
import com.example.dto.OrderListDto;
import com.example.dto.OrderPayCheckDto;
import com.example.dto.OrderPayDto;
import com.example.dto.OrderTicketUserCreateDto;
import com.example.dto.PayDto;
import com.example.dto.ProgramOperateDataDto;
import com.example.dto.TradeCheckDto;
import com.example.dto.UserGetAndTicketUserListDto;
import com.example.entity.Order;
import com.example.entity.OrderTicketUser;
import com.example.entity.OrderTicketUserAggregate;
import com.example.enums.BaseCode;
import com.example.enums.OrderStatus;
import com.example.enums.PayBillStatus;
import com.example.enums.PayChannel;
import com.example.enums.SellStatus;
import com.example.exception.CookFrameException;
import com.example.mapper.OrderMapper;
import com.example.mapper.OrderTicketUserMapper;
import com.example.redis.RedisCache;
import com.example.redis.RedisKeyWrap;
import com.example.service.delaysend.DelayOperateProgramDataSend;
import com.example.service.properties.OrderProperties;
import com.example.servicelock.annotion.ServiceLock;
import com.example.util.DateUtils;
import com.example.vo.NotifyVo;
import com.example.vo.OrderGetVo;
import com.example.vo.OrderListVo;
import com.example.vo.OrderPayCheckVo;
import com.example.vo.OrderTicketUserVo;
import com.example.vo.SeatVo;
import com.example.vo.TicketUserInfoVo;
import com.example.vo.TradeCheckVo;
import com.example.vo.UserAndTicketUserInfoVo;
import com.example.vo.UserGetAndTicketUserListVo;
import com.example.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.constant.Constant.ALIPAY_NOTIFY_SUCCESS_RESULT;
import static com.example.core.DistributedLockConstants.ORDER_CANCEL_LOCK;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-12
 */
@Slf4j
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
    
    @Autowired
    private PayClient payClient;
    
    @Autowired
    private UserClient userClient;
    
    @Autowired
    private OrderProperties orderProperties;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private DelayOperateProgramDataSend delayOperateProgramDataSend;
    
    @Transactional(rollbackFor = Exception.class)
    public String create(OrderCreateDto orderCreateDto) {
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper = 
                Wrappers.lambdaQuery(Order.class).eq(Order::getOrderNumber, orderCreateDto.getOrderNumber());
        Order oldOrder = orderMapper.selectOne(orderLambdaQueryWrapper);
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
        return String.valueOf(order.getOrderNumber());
    }
    
    @Transactional(rollbackFor = Exception.class)
    @ServiceLock(name = ORDER_CANCEL_LOCK,keys = {"#orderCancelDto.orderId"})
    public boolean cancel(OrderCancelDto orderCancelDto){
        updateOrderRelatedData(orderCancelDto.getOrderNumber(),OrderStatus.CANCEL);
        return true;
    }
    
    public String pay(OrderPayDto orderPayDto) {
        Long orderNumber = orderPayDto.getOrderNumber();
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper =
                Wrappers.lambdaQuery(Order.class).eq(Order::getOrderNumber, orderNumber);
        Order order = orderMapper.selectOne(orderLambdaQueryWrapper);
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
        if (orderPayDto.getPrice().compareTo(order.getOrderPrice()) != 0) {
            throw new CookFrameException(BaseCode.PAY_PRICE_NOT_EQUAL_ORDER_PRICE);
        }
        //调用支付服务进行支付
        PayDto payDto = new PayDto();
        payDto.setOrderNumber(String.valueOf(orderNumber));
        payDto.setPayBillType(orderPayDto.getPayBillType());
        payDto.setSubject(orderPayDto.getSubject());
        payDto.setChannel(orderPayDto.getChannel());
        payDto.setPlatform(orderPayDto.getPlatform());
        payDto.setPrice(orderPayDto.getPrice());
        payDto.setNotifyUrl(orderProperties.getOrderPayNotifyUrl());
        payDto.setReturnUrl(orderProperties.getOrderPayReturnUrl());
        ApiResponse<String> payResponse = payClient.commonPay(payDto);
        if (!Objects.equals(payResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            throw new CookFrameException(payResponse);
        }
        return payResponse.getData();
    }
    
    @Transactional(rollbackFor = Exception.class)
    public OrderPayCheckVo payCheck(OrderPayCheckDto orderPayCheckDto){
        OrderPayCheckVo orderPayCheckVo = new OrderPayCheckVo();
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper =
                Wrappers.lambdaQuery(Order.class).eq(Order::getOrderNumber, orderPayCheckDto.getOrderNumber());
        Order order = orderMapper.selectOne(orderLambdaQueryWrapper);
        if (Objects.isNull(order)) {
            throw new CookFrameException(BaseCode.ORDER_NOT_EXIST);
        }
        BeanUtil.copyProperties(order,orderPayCheckVo);
        TradeCheckDto tradeCheckDto = new TradeCheckDto();
        tradeCheckDto.setOutTradeNo(String.valueOf(orderPayCheckDto.getOrderNumber()));
        tradeCheckDto.setChannel(Optional.ofNullable(PayChannel.getRc(orderPayCheckDto.getPayChannelType()))
                .map(PayChannel::getValue).orElseThrow(() -> new CookFrameException(BaseCode.PAY_CHANNEL_NOT_EXIST)));
        ApiResponse<TradeCheckVo> tradeCheckVoApiResponse = payClient.tradeCheck(tradeCheckDto);
        if (!Objects.equals(tradeCheckVoApiResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            throw new CookFrameException(tradeCheckVoApiResponse);
        }
        TradeCheckVo tradeCheckVo = Optional.ofNullable(tradeCheckVoApiResponse.getData())
                .orElseThrow(() -> new CookFrameException(BaseCode.PAY_BILL_NOT_EXIST));
        if (tradeCheckVo.isSuccess()) {
            Integer payBillStatus = tradeCheckVo.getPayBillStatus();
            Integer orderStatus = order.getOrderStatus();
            if (!Objects.equals(orderStatus, payBillStatus)) {
                Order updateOrder = new Order();
                updateOrder.setId(order.getId());
                updateOrder.setOrderStatus(payBillStatus);
                orderPayCheckVo.setOrderStatus(payBillStatus);
                if (Objects.equals(payBillStatus, PayBillStatus.PAY.getCode())) {
                    updateOrder.setPayOrderTime(DateUtils.now());
                    orderPayCheckVo.setPayOrderTime(DateUtils.now());
                }else if (Objects.equals(payBillStatus, PayBillStatus.CANCEL.getCode())) {
                    updateOrder.setCancelOrderTime(DateUtils.now());
                    orderPayCheckVo.setCancelOrderTime(DateUtils.now());
                }
                //更新订单状态
                orderMapper.updateById(updateOrder);
            }
            //将订单更新为支付状态
            if (Objects.equals(payBillStatus, PayBillStatus.PAY.getCode())) {
                orderService.updateOrderRelatedData(order.getId(),OrderStatus.PAY);
                //将订单更新为取消状态
            } else if (Objects.equals(payBillStatus, PayBillStatus.CANCEL.getCode())) {
                orderService.updateOrderRelatedData(order.getId(),OrderStatus.CANCEL);
            }
            
        }else {
            throw new CookFrameException(BaseCode.PAY_TRADE_CHECK_ERROR);
        }
        return orderPayCheckVo;
    }
    
    
    @ServiceLock(name = ORDER_CANCEL_LOCK,keys = {"#outTradeNo"})
    public String alipayNotify(Map<String, String> params, String outTradeNo){
        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setChannel(PayChannel.ALIPAY.getValue());
        notifyDto.setParams(params);
        ApiResponse<NotifyVo> notifyResponse = payClient.notify(notifyDto);
        if (!Objects.equals(notifyResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            throw new CookFrameException(notifyResponse);
        }
        //将订单状态更新
        if (ALIPAY_NOTIFY_SUCCESS_RESULT.equals(notifyResponse.getData().getPayResult())) {
            orderService.updateOrderRelatedData(Long.parseLong(notifyResponse.getData().getOutTradeNo()),OrderStatus.PAY);
        }
        return notifyResponse.getData().getPayResult();
    }
    
    /**
     * 更新订单和购票人订单状态以及操作缓存数据
     * */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderRelatedData(Long orderNumber,OrderStatus orderStatus){
        if (!(Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode()) ||
                Objects.equals(orderStatus.getCode(), OrderStatus.PAY.getCode()))) {
            throw new CookFrameException(BaseCode.OPERATE_ORDER_STATUS_NOT_PERMIT);
        }
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper =
                Wrappers.lambdaQuery(Order.class).eq(Order::getOrderNumber, orderNumber);
        Order order = orderMapper.selectOne(orderLambdaQueryWrapper);
        if (Objects.isNull(order)) {
            throw new CookFrameException(BaseCode.ORDER_NOT_EXIST);
        }
        if (Objects.equals(order.getOrderStatus(), OrderStatus.CANCEL.getCode())) {
            log.info("订单已取消 orderNumber : {}",orderNumber);
            return;
        }
        if (Objects.equals(order.getOrderStatus(), OrderStatus.PAY.getCode())) {
            log.info("订单已支付 orderNumber : {}",orderNumber);
            return;
        }
        if (Objects.equals(order.getOrderStatus(), OrderStatus.REFUND.getCode())) {
            log.info("订单已退单 orderNumber : {}",orderNumber);
            return;
        }
        //将订单更新为取消或者支付状态
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setOrderStatus(orderStatus.getCode());
        updateOrder.setCancelOrderTime(DateUtils.now());
        int updateOrderResult = orderMapper.updateById(updateOrder);
        //将购票人订单更新为取消或者支付状态
        OrderTicketUser updateOrderTicketUser = new OrderTicketUser();
        updateOrderTicketUser.setOrderStatus(orderStatus.getCode());
        updateOrderTicketUser.setCancelOrderTime(DateUtils.now());
        
        LambdaUpdateWrapper<OrderTicketUser> orderTicketUserLambdaUpdateWrapper =
                Wrappers.lambdaUpdate(OrderTicketUser.class).eq(OrderTicketUser::getOrderNumber, order.getOrderNumber());
        
        int updateTicketUserOrderResult =
                orderTicketUserMapper.update(updateOrderTicketUser,orderTicketUserLambdaUpdateWrapper);
        if (updateOrderResult <= 0 || updateTicketUserOrderResult <= 0) {
            throw new CookFrameException(BaseCode.ORDER_CANAL_ERROR);
        }
        
        LambdaQueryWrapper<OrderTicketUser> orderTicketUserLambdaQueryWrapper =
                Wrappers.lambdaQuery(OrderTicketUser.class).eq(OrderTicketUser::getOrderNumber, order.getOrderNumber());
        List<OrderTicketUser> orderTicketUserList = orderTicketUserMapper.selectList(orderTicketUserLambdaQueryWrapper);
        if (CollectionUtil.isEmpty(orderTicketUserList)) {
            throw new CookFrameException(BaseCode.TICKET_USER_ORDER_NOT_EXIST);
        }
        Long programId = orderTicketUserList.get(0).getProgramId();
        //查询到购票人的座位
        List<String> seatIdList =
                orderTicketUserList.stream().map(OrderTicketUser::getSeatId).map(String::valueOf).collect(Collectors.toList());
        //从redis中查询锁定中的座位
        List<SeatVo> seatVoList = redisCache.multiGetForHash(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId), seatIdList, SeatVo.class);
        if (CollectionUtil.isEmpty(seatVoList)) {
            throw new CookFrameException(BaseCode.LOCK_SEAT_LIST_EMPTY);
        }
        
        /**=====操作缓存相关数据====*/
        //redis解除锁座位
        List<String> unLockSeatIdList = seatVoList.stream().map(SeatVo::getId).map(String::valueOf).collect(Collectors.toList());
        Map<String, SeatVo> unLockSeatVoMap = seatVoList.stream().collect(Collectors
                .toMap(seatVo -> String.valueOf(seatVo.getId()), seatVo -> seatVo, (v1, v2) -> v2));
        List<String> seatDataList = new ArrayList<>();
        unLockSeatVoMap.forEach((k,v) -> {
            seatDataList.add(k);
            if (Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode())){
                v.setSellStatus(SellStatus.NO_SOLD.getCode());
            }else if (Objects.equals(orderStatus.getCode(), OrderStatus.PAY.getCode())) {
                v.setSellStatus(SellStatus.SOLD.getCode());
            }
            seatDataList.add(JSON.toJSONString(v));
        });
        
        List<String> keys = new ArrayList<>();
        //操作类型
        keys.add(String.valueOf(orderStatus.getCode()));
        //锁定座位的key
        keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_LOCK_HASH, programId).getRelKey());
        
        Object[] data = new String[3];
        //扣除锁定的座位数据
        data[0] = JSON.toJSONString(unLockSeatIdList);
        //如果是订单取消的操作，那么添加到未售卖的座位数据
        //如果是订单支付的操作，那么添加到已售卖的座位数据
        data[1] = JSON.toJSONString(seatDataList);
        
        //根据座位集合统计出对应的票档数量
        Map<Long, Long> ticketCategoryCountMap = seatVoList.stream().collect(Collectors.groupingBy(SeatVo::getTicketCategoryId, Collectors.counting()));
        JSONArray jsonArray = new JSONArray();
        ticketCategoryCountMap.forEach((k,v) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ticketCategoryId",String.valueOf(k));
            jsonObject.put("count",v);
            jsonArray.add(jsonObject);
        });
        
        if (Objects.equals(orderStatus.getCode(), OrderStatus.CANCEL.getCode())) {
            //没有售卖座位的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_NO_SOLD_HASH, programId).getRelKey());
            //恢复库存的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_TICKET_REMAIN_NUMBER_HASH, programId).getRelKey());
            //恢复库存数据
            data[2] = JSON.toJSONString(jsonArray);
        }else if (Objects.equals(orderStatus.getCode(), OrderStatus.PAY.getCode())) {
            //已售卖座位的key
            keys.add(RedisKeyWrap.createRedisKey(RedisKeyEnum.PROGRAM_SEAT_SOLD_HASH, programId).getRelKey());
        }
        programCacheReverseOperate.programCacheReverseOperate(keys,data);
        
        //如果是支付成功了，发送延迟队列给program服务，将数据库中的票档的余票更新、座位状态更新
        if (Objects.equals(orderStatus.getCode(), OrderStatus.PAY.getCode())) {
            ProgramOperateDataDto programOperateDataDto = new ProgramOperateDataDto();
            programOperateDataDto.setProgramId(programId);
            programOperateDataDto.setSeatIdList(JSON.parseArray((String)data[0],Long.class));
            programOperateDataDto.setTicketCategoryCountMap(ticketCategoryCountMap);
            programOperateDataDto.setSellStatus(SellStatus.SOLD.getCode());
            delayOperateProgramDataSend.sendMessage(JSON.toJSONString(programOperateDataDto));
        }
    }
    
    public List<OrderListVo> selectList(OrderListDto orderListDto) {
        List<OrderListVo> orderListVos = new ArrayList<>();
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper = 
                Wrappers.lambdaQuery(Order.class).eq(Order::getUserId, orderListDto.getUserId());
        List<Order> orderList = orderMapper.selectList(orderLambdaQueryWrapper);
        if (CollectionUtil.isEmpty(orderList)) {
            return orderListVos;
        }
        orderListVos = BeanUtil.copyToList(orderList, OrderListVo.class);
        //每个订单下的购票人订单数量统计
        List<OrderTicketUserAggregate> orderTicketUserAggregateList = 
                orderTicketUserMapper.selectOrderTicketUserAggregate(orderList.stream().map(Order::getOrderNumber).collect(Collectors.toList()));
        Map<Long, Integer> orderTicketUserAggregateMap = orderTicketUserAggregateList.stream()
                .collect(Collectors.toMap(OrderTicketUserAggregate::getOrderNumber, 
                        OrderTicketUserAggregate::getOrderTicketUserCount, (v1, v2) -> v2));
        for (OrderListVo orderListVo : orderListVos) {
            orderListVo.setTicketCount(orderTicketUserAggregateMap.get(orderListVo.getOrderNumber()));
        }
        return orderListVos;
    }
    
    public OrderGetVo get(OrderGetDto orderGetDto) {
        //查询订单
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper =
                Wrappers.lambdaQuery(Order.class).eq(Order::getOrderNumber, orderGetDto.getOrderNumber());
        Order order = orderMapper.selectOne(orderLambdaQueryWrapper);
        if (Objects.isNull(order)) {
            throw new CookFrameException(BaseCode.ORDER_NOT_EXIST);
        }
        //查询购票人订单
        LambdaQueryWrapper<OrderTicketUser> orderTicketUserLambdaQueryWrapper = 
                Wrappers.lambdaQuery(OrderTicketUser.class).eq(OrderTicketUser::getOrderNumber, order.getOrderNumber());
        List<OrderTicketUser> orderTicketUserList = orderTicketUserMapper.selectList(orderTicketUserLambdaQueryWrapper);
        if (CollectionUtil.isEmpty(orderTicketUserList)) {
            throw new CookFrameException(BaseCode.TICKET_USER_ORDER_NOT_EXIST);   
        }
        
        OrderGetVo orderGetVo = new OrderGetVo();
        BeanUtil.copyProperties(order,orderGetVo);
        
        orderGetVo.setOrderTicketUserVoList(BeanUtil.copyToList(orderTicketUserList, OrderTicketUserVo.class));
        
        //查询用户和购票人信息
        UserGetAndTicketUserListDto userGetAndTicketUserListDto = new UserGetAndTicketUserListDto();
        userGetAndTicketUserListDto.setUserId(order.getUserId());
        userGetAndTicketUserListDto.setTicketUserIdList(orderTicketUserList.stream()
                .map(OrderTicketUser::getTicketUserId).collect(Collectors.toList()));
        ApiResponse<UserGetAndTicketUserListVo> userGetAndTicketUserApiResponse = 
                userClient.getUserAndTicketUserList(userGetAndTicketUserListDto);
        
        if (!Objects.equals(userGetAndTicketUserApiResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            throw new CookFrameException(userGetAndTicketUserApiResponse);
            
        }
        //验证用户和购票人信息是否存在
        UserGetAndTicketUserListVo userAndTicketUserListVo =
                Optional.ofNullable(userGetAndTicketUserApiResponse.getData())
                        .orElseThrow(() -> new CookFrameException(BaseCode.RPC_RESULT_DATA_EMPTY));
        if (Objects.isNull(userAndTicketUserListVo.getUserVo())) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        if (CollectionUtil.isEmpty(userAndTicketUserListVo.getTicketUserVoList())) {
            throw new CookFrameException(BaseCode.TICKET_USER_EMPTY);
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userAndTicketUserListVo.getUserVo(),userInfoVo);
        UserAndTicketUserInfoVo userAndTicketUserInfoVo = new UserAndTicketUserInfoVo();
        userAndTicketUserInfoVo.setUserInfoVo(userInfoVo);
        userAndTicketUserInfoVo.setTicketUserInfoVoList(BeanUtil.copyToList(userAndTicketUserListVo.getTicketUserVoList(), TicketUserInfoVo.class));
        orderGetVo.setUserAndTicketUserInfoVo(userAndTicketUserInfoVo);
        
        return orderGetVo;
    }
}
