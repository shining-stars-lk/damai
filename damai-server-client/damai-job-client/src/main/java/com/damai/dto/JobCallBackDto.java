package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job回调  dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="JobCallBackDto", description ="job回调")
public class JobCallBackDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="jobId", dataType ="String", value ="任务id", required =true)
    @NotBlank
    private String jobId;
    
    @ApiModelProperty(name ="runInfo", dataType ="String", value ="执行成功或失败信息", required =true)
    @NotBlank
    private String runInfo;
    
    @ApiModelProperty(name ="runStatus", dataType ="Integer", value ="执行结果状态 1:新建 2:执行完成 3:执行失败 JobRunStatus", required =true)
    @NotNull
    private Integer runStatus;
    
    @ApiModelProperty(name ="currentResidueRetryNumber", dataType ="String", value ="重试次数")
    private Integer currentResidueRetryNumber;
}
