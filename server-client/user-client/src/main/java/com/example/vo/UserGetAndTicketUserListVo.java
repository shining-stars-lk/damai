package com.example.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="UserGetAndTicketUserListVo", description ="用户和购票人集合数据")
public class UserGetAndTicketUserListVo {
    
    @ApiModelProperty(name ="userVo", dataType ="UserVo", value ="用户")
    private UserVo userVo;
    
    @ApiModelProperty(name ="ticketUserVoList", dataType ="List<TicketUserVo>", value ="购票人集合")
    private List<TicketUserVo> ticketUserVoList;
}
