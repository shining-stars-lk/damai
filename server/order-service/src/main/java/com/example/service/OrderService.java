package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.OrderCreateDto;
import com.example.dto.OrderTicketUserCreateDto;
import com.example.entity.Order;
import com.example.entity.OrderTicketUser;
import com.example.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private OrderTicketUserService orderTicketUserService;
    
    public String create(final OrderCreateDto orderCreateDto) {
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
}
