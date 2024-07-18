package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job回调  dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="JobCallBackDto", description ="job回调")
public class JobCallBackDto {
    
    @Schema(name ="id", type ="String", description ="id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long id;
    
    @Schema(name ="jobId", type ="String", description ="任务id", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String jobId;
    
    @Schema(name ="runInfo", type ="String", description ="执行成功或失败信息", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String runInfo;
    
    @Schema(name ="runStatus", type ="Integer", description ="执行结果状态 1:新建 2:执行完成 3:执行失败 JobRunStatus", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer runStatus;
    
    @Schema(name ="currentResidueRetryNumber", type ="String", description ="重试次数")
    private Integer currentResidueRetryNumber;
}
