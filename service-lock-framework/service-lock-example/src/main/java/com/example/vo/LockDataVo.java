package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-17
 **/
@Data
@ApiModel(value="LockDataVo", description ="分布式锁测试数据")
public class LockDataVo {
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="id", required =true)
    private Long id;
    
    @ApiModelProperty(name ="stock", dataType ="Integer", value ="库存", required =true)
    private Integer stock;
}
