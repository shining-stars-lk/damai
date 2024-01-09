package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="TicketUserIdDto", description ="购票人id入参")
public class TicketUserIdDto {
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="购票人id", required =true)
    @NotBlank
    private Long id;
}