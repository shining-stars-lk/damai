package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 账单 dto
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="PayDto", description ="支付")
public class PayBillDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单号",required = true)
    @NotNull
    private String orderNumber;
}
