package com.example.service;


import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.TicketUserDto;
import com.example.dto.TicketUserIdDto;
import com.example.dto.TicketUserListDto;
import com.example.entity.TicketUser;
import com.example.entity.User;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import com.example.mapper.TicketUserMapper;
import com.example.mapper.UserMapper;
import com.example.vo.TicketUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 购票人表 服务实现类
 * </p>
 *
 * @author k
 * @since 2024-01-09
 */
@Service
public class TicketUserService extends ServiceImpl<TicketUserMapper, TicketUser> {
    
    @Autowired
    private TicketUserMapper ticketUserMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    public List<TicketUserVo> select(TicketUserListDto ticketUserListDto) {
        LambdaQueryWrapper<TicketUser> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUser.class)
                .eq(TicketUser::getUserId, ticketUserListDto.getUserId());
        List<TicketUser> ticketUsers = ticketUserMapper.selectList(ticketUserLambdaQueryWrapper);
        return BeanUtil.copyToList(ticketUsers,TicketUserVo.class);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void add(final TicketUserDto ticketUserDto) {
        User user = userMapper.selectById(ticketUserDto.getUserId());
        if (Objects.isNull(user)) {
            throw new CookFrameException(BaseCode.USER_EMPTY);
        }
        LambdaQueryWrapper<TicketUser> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUser.class)
                .eq(TicketUser::getUserId, ticketUserDto.getUserId())
                .eq(TicketUser::getIdType, ticketUserDto.getIdType())
                .eq(TicketUser::getIdNumber, ticketUserDto.getIdNumber());
        TicketUser ticketUser = ticketUserMapper.selectOne(ticketUserLambdaQueryWrapper);
        if (Objects.nonNull(ticketUser)) {
            throw new CookFrameException(BaseCode.TICKET_USER_EXIST);
        }
        TicketUser addTicketUser = new TicketUser();
        BeanUtil.copyProperties(ticketUserDto,addTicketUser);
        addTicketUser.setId(uidGenerator.getUID());
        ticketUserMapper.insert(addTicketUser);
    }
    @Transactional(rollbackFor = Exception.class)
    public void delete(final TicketUserIdDto ticketUserIdDto) {
        TicketUser ticketUser = ticketUserMapper.selectById(ticketUserIdDto.getId());
        if (Objects.isNull(ticketUser)) {
            throw new CookFrameException(BaseCode.TICKET_USER_EMPTY);
        }
        ticketUserMapper.deleteById(ticketUserIdDto.getId());
    }
}
