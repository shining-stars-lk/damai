package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目简单信息 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramSimpleInfoVo", description ="节目简单信息")
public class ProgramSimpleInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="id", type ="Long", description ="主键id")
    private Long programId;
    
    @Schema(name ="areaId", type ="Long", description ="地区id")
    private Long areaId;
    
    @Schema(name ="areaIdName", type ="String", description ="地区名字")
    private String areaIdName;
}

