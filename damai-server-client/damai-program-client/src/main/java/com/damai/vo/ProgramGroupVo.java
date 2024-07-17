package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目分组 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramGroupVo", description ="节目分组")
public class ProgramGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="id", type ="Long", description ="主键id")
    private Long id;
    
    @Schema(name ="programSimpleInfoVoList", type ="List<ProgramSimpleInfoVo>", description ="节目简单信息集合")
    private List<ProgramSimpleInfoVo> programSimpleInfoVoList;
    
    @Schema(name ="recentShowTime", type ="Date", description ="最近的节目演出时间")
    private Date recentShowTime;
}
