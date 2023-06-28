package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class JobCallBackDto {
    
    @NotBlank
    private String id;
    
    @NotBlank
    private String jobId;
    
    @NotBlank
    private String runInfo;
    
    @NotNull
    private Integer runStatus;

    private Integer currentResidueRetryNumber;
}
