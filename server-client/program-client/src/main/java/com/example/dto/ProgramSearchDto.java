package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-05
 **/
@Data
@ApiModel(value="ProgramSearchDto", description ="节目搜索")
public class ProgramSearchDto extends BasePageDto{
    
    @ApiModelProperty(name ="title", dataType ="String", value ="标题")
    @NotBlank
    private String title;
}
