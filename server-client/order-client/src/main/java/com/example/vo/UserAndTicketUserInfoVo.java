package com.example.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="UserAndTicketUserInfoVo", description ="用户和购票人集合数据")
public class UserAndTicketUserInfoVo {
    
    @ApiModelProperty(name ="userInfoVo", dataType ="UserInfoVo", value ="用户")
    private UserInfoVo userInfoVo;
    
    @ApiModelProperty(name ="ticketUserInfoVoList", dataType ="List<TicketUserInfoVo>", value ="购票人集合")
    private List<TicketUserInfoVo> ticketUserInfoVoList;
}
