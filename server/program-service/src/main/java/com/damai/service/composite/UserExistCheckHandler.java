package com.damai.service.composite;

import com.alibaba.fastjson.JSON;
import com.damai.client.UserClient;
import com.damai.common.ApiResponse;
import com.damai.composite.AbstractComposite;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.dto.UserGetAndTicketUserListDto;
import com.damai.enums.BaseCode;
import com.damai.enums.CompositeCheckType;
import com.damai.exception.DaMaiFrameException;
import com.damai.vo.TicketUserVo;
import com.damai.vo.UserGetAndTicketUserListVo;
import com.damai.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户检查
 * @author: 阿宽不是程序员
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
                            .orElseThrow(() -> new DaMaiFrameException(BaseCode.RPC_RESULT_DATA_EMPTY));
            if (Objects.isNull(userAndTicketUserListVo.getUserVo())) {
                throw new DaMaiFrameException(BaseCode.USER_EMPTY);
            }
            ticketUserVoList =
                    Optional.ofNullable(userAndTicketUserListVo.getTicketUserVoList()).filter(list -> !list.isEmpty())
                            .orElseThrow(() -> new DaMaiFrameException(BaseCode.TICKET_USER_EMPTY));
            log.info("userVo : {}, ticketUserVoList : {}",JSON.toJSONString(userVo),JSON.toJSONString(ticketUserVoList));
        }else {
            log.error("user client rpc getUserAndTicketUserList error response : {}", JSON.toJSONString(userGetAndTicketUserApiResponse));
            throw new DaMaiFrameException(userGetAndTicketUserApiResponse);
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
        return 5;
    }
}
