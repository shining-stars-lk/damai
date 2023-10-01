package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="PageDto", description ="PageDto")
public class PageDto {

    @ApiModelProperty(name ="pageNo", dataType ="Long", value ="当前页码", required =true)
    @NotNull
    private Long pageNo;

    @ApiModelProperty(name ="pageSize", dataType ="Long", value ="页数大小", required =true)
    @NotNull
    private Long pageSize;
}
