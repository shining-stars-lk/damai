package com.damai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: api调用数据 dto
 * @author: 阿星不是程序员
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;
    
    @ApiModelProperty(name ="endDate", dataType ="String", value ="结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endDate;
    
    @ApiModelProperty(name ="apiAddress", dataType ="String", value ="api的ip地址")
    private String apiAddress;
    
    @ApiModelProperty(name ="apiUrl", dataType ="String", value ="api路径")
    private String apiUrl;
}
