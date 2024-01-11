package com.example.service;

import com.example.dto.ProgramOrderDto;
import com.example.entity.Program;
import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;
import com.example.mapper.ProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-11
 **/
@Service
public class ProgramOrderService {
    
    @Autowired
    private ProgramMapper programMapper;
    
    public void create(final ProgramOrderDto programOrderDto) {
        Program program = programMapper.selectById(programOrderDto.getProgramId());
        if (Objects.isNull(program)) {
            throw new CookFrameException(BaseCode.PROGRAM_NOT_EXIST);
        }
        
        
    }
}
