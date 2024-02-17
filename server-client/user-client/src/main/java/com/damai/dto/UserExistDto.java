package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@Data
@ApiModel(value="UserExistDto", description ="用户是否存在")
public class UserExistDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="mobile", dataType ="String", value ="手机号",required = true)
    @NotBlank
    private String mobile;
    
}
