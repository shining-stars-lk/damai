package com.damai.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户和购票人 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="UserGetAndTicketUserListVo", description ="用户和购票人集合数据")
public class UserGetAndTicketUserListVo {
    
    @Schema(name ="userVo", type ="UserVo", description ="用户")
    private UserVo userVo;
    
    @Schema(name ="ticketUserVoList", type ="List<TicketUserVo>", description ="购票人集合")
    private List<TicketUserVo> ticketUserVoList;
}
