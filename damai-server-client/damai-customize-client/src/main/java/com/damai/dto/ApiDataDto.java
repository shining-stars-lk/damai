package com.damai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: api调用数据 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ApiDataDto", description ="api被限制调用记录")
public class ApiDataDto {
    
    @Schema(name ="pageNo", type ="Integer", description ="页码", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer pageNo;
    
    @Schema(name ="pageSize", type ="Integer", description ="页数", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer pageSize;
    
    @Schema(name ="startDate", type ="String", description ="开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;
    
    @Schema(name ="endDate", type ="String", description ="结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endDate;
    
    @Schema(name ="apiAddress", type ="String", description ="api的ip地址")
    private String apiAddress;
    
    @Schema(name ="apiUrl", type ="String", description ="api路径")
    private String apiUrl;
}
