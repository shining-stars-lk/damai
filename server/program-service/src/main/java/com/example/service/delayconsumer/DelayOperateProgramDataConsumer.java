package com.example.service.delayconsumer;

import com.alibaba.fastjson.JSON;
import com.example.core.ConsumerTask;
import com.example.core.StringUtil;
import com.example.dto.ProgramOperateDataDto;
import com.example.service.ProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.service.constant.ProgramOrderConstant.DELAY_OPERATE_PROGRAM_DATA_TOPIC;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-21
 **/
@Slf4j
@Component
public class DelayOperateProgramDataConsumer implements ConsumerTask {
    
    @Autowired
    private ProgramService programService;
    
    @Override
    public void execute(String content) {
        log.info("延迟操作节目数据消息进行消费 content : {}", content);
        if (StringUtil.isEmpty(content)) {
            log.error("延迟队列消息不存在");
            return;
        }
        ProgramOperateDataDto programOperateDataDto = JSON.parseObject(content, ProgramOperateDataDto.class);
        programService.OperateProgramData(programOperateDataDto);
    }
    
    @Override
    public String topic() {
        return DELAY_OPERATE_PROGRAM_DATA_TOPIC;
    }
}
