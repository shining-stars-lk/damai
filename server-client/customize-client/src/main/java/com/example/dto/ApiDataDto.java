package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
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
