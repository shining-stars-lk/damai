package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="ApiDataDto", description ="api被限制调用记录")
public class ApiDataDto {
    
    @ApiModelProperty(name ="pageNo", dataType ="Integer", value ="页码", required =true)
    @NotNull
    private Integer pageNo;
    
    @ApiModelProperty(name ="pageSize", dataType ="Integer", value ="页数", required =true)
    @NotNull
    private Integer pageSize;
    
    @ApiModelProperty(name ="startDate", dataType ="String", value ="开始日期")
    private String startDate;
    
    @ApiModelProperty(name ="endDate", dataType ="String", value ="结束日期")
    private String endDate;
    
    @ApiModelProperty(name ="apiAddress", dataType ="String", value ="api的ip地址")
    private String apiAddress;
    
    @ApiModelProperty(name ="apiUrl", dataType ="String", value ="api路径")
    private String apiUrl;
}
