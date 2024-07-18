package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job任务查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="JobInfoPageDto", description ="job任务查询")
public class JobInfoPageDto {
    
    @Schema(name ="pageSize", type ="Integer", description ="页码", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer pageSize;
    
    @Schema(name ="pageNo", type ="Integer", description ="页容量", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer pageNo;
}
