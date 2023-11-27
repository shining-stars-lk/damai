package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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
