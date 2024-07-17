package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 广播调用 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="BroadcastCallDto", description ="广播调用")
public class BroadcastCallDto {
    
    @Schema(name ="serviceName", type ="String", description ="服务名", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String serviceName;
    
    @Schema(name ="requestBody", type ="String", description ="请求体", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String requestBody;
}
