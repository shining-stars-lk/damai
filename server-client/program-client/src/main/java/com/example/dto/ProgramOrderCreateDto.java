package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-11
 **/
@Data
@ApiModel(value="ProgramOrderCreateDto", description ="节目订单创建")
public class ProgramOrderCreateDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目id")
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id")
    @NotNull
    private Long userId;
    
    @ApiModelProperty(name ="programOrderTicketUserDtoList", dataType ="List<ProgramOrderTicketUserDto>", value ="购票人集合")
    @NotNull
    private List<ProgramOrderTicketUserDto> programOrderTicketUserDtoList;
    
    @ApiModelProperty(name ="seatDtoList", dataType ="List<SeatDto>", value = "座位")
    private List<SeatDto> seatDtoList;
}
