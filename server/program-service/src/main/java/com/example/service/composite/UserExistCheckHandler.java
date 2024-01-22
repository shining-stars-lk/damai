package com.example.service.composite;

import com.alibaba.fastjson.JSON;
import com.example.client.UserClient;
import com.example.common.ApiResponse;
import com.example.composite.AbstractComposite;
import com.example.dto.ProgramOrderCreateDto;
import com.example.dto.UserGetAndTicketUserListDto;
import com.example.enums.BaseCode;
import com.example.enums.CompositeCheckType;
import com.example.exception.CookFrameException;
import com.example.vo.TicketUserVo;
import com.example.vo.UserGetAndTicketUserListVo;
import com.example.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-22
 **/
@Slf4j
@Component
public class UserExistCheckHandler extends AbstractComposite<ProgramOrderCreateDto> {
    
    @Autowired
    private UserClient userClient;
    
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        //验证用户和购票人信息正确性
        UserVo userVo = new UserVo();
        List<TicketUserVo> ticketUserVoList = new ArrayList<>();
        UserGetAndTicketUserListDto userGetAndTicketUserListDto = new UserGetAndTicketUserListDto();
        userGetAndTicketUserListDto.setUserId(programOrderCreateDto.getUserId());
        userGetAndTicketUserListDto.setTicketUserIdList(programOrderCreateDto.getTicketUserIdList());
        ApiResponse<UserGetAndTicketUserListVo> userGetAndTicketUserApiResponse =
                userClient.getUserAndTicketUserList(userGetAndTicketUserListDto);
        if (Objects.equals(userGetAndTicketUserApiResponse.getCode(), BaseCode.SUCCESS.getCode())) {
            UserGetAndTicketUserListVo userAndTicketUserListVo =
                    Optional.ofNullable(userGetAndTicketUserApiResponse.getData())
                            .orElseThrow(() -> new CookFrameException(BaseCode.RPC_RESULT_DATA_EMPTY));
            if (Objects.isNull(userAndTicketUserListVo.getUserVo())) {
                throw new CookFrameException(BaseCode.USER_EMPTY);
            }
            ticketUserVoList =
                    Optional.ofNullable(userAndTicketUserListVo.getTicketUserVoList()).filter(list -> !list.isEmpty())
                            .orElseThrow(() -> new CookFrameException(BaseCode.TICKET_USER_EMPTY));
            log.info("userVo : {}, ticketUserVoList : {}",JSON.toJSONString(userVo),JSON.toJSONString(ticketUserVoList));
        }else {
            log.error("user client rpc getUserAndTicketUserList error response : {}", JSON.toJSONString(userGetAndTicketUserApiResponse));
            throw new CookFrameException(userGetAndTicketUserApiResponse);
        }
    }
    
    @Override
    public String type() {
        return CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue();
    }
    
    @Override
    public Integer executeParentOrder() {
        return 0;
    }
    
    @Override
    public Integer executeTier() {
        return 1;
    }
    
    @Override
    public Integer executeOrder() {
        return 3;
    }
}
