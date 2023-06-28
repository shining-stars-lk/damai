package com.example.client;

import com.example.common.Result;
import com.example.dto.JobCallBackDto;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Component
public class JobClientFallback implements JobClient {
    
    @Override
    public Result<Boolean> timerNotice(final JobCallBackDto dto) {
        return Result.error();
    }
}
