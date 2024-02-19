package com.damai.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户和购票人 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="UserGetAndTicketUserListVo", description ="用户和购票人集合数据")
public class UserGetAndTicketUserListVo {
    
    @ApiModelProperty(name ="userVo", dataType ="UserVo", value ="用户")
    private UserVo userVo;
    
    @ApiModelProperty(name ="ticketUserVoList", dataType ="List<TicketUserVo>", value ="购票人集合")
    private List<TicketUserVo> ticketUserVoList;
}
