package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@Data
@ApiModel(value="MethodChainDto", description ="MethodChainDto")
public class MethodChainDto {
    
    @ApiModelProperty(name ="methodDetailDataId", dataType ="String", value ="MethodDetailDataId", required =true)
    @NotBlank
    private String methodDetailDataId;
}
