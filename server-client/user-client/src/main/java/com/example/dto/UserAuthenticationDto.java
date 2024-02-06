package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel(value="UserAuthenticationDto", description ="用户实名认证")
public class UserAuthenticationDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="用户id",required = true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="relName", dataType ="String", value ="用户真实名字")
    @NotBlank
    private String relName;
    
    @ApiModelProperty(name ="idNumber", dataType ="String", value ="身份证号码")
    @NotBlank
    private String idNumber;
    
}
