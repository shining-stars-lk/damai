package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户和购票人查询 dto
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="UserGetAndTicketUserListDto", description ="查询用户和购票人集合入参")
public class UserGetAndTicketUserListDto {
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="用户id", required =true)
    @NotNull
    private Long userId;
    
    @ApiModelProperty(name ="ticketUserIdList", dataType ="List<Long>", value ="购票人id集合", required =true)
    @NotNull
    private List<Long> ticketUserIdList;
}