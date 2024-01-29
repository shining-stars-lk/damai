package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

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