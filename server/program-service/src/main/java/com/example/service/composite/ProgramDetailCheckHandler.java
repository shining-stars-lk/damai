package com.example.service.composite;

import com.example.composite.AbstractComposite;
import com.example.dto.ProgramGetDto;
import com.example.dto.ProgramOrderCreateDto;
import com.example.enums.CompositeCheckType;
import com.example.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-22
 **/
@Component
public class ProgramDetailCheckHandler extends AbstractComposite<ProgramOrderCreateDto> {
    
    @Autowired
    private ProgramService programService;
    @Override
    protected void execute(final ProgramOrderCreateDto programOrderCreateDto) {
        //避免节目不存在，再次缓存
        ProgramGetDto programGetDto = new ProgramGetDto();
        programGetDto.setId(programOrderCreateDto.getProgramId());
        programService.getDetail(programGetDto);
    }
    
    @Override
    public String type() {
        return CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue();
    }
    
    @Override
    public Integer executeParentOrder() {
        return 0;
    }
    
    @Override
    public Integer executeTier() {
        return 1;
    }
    
    @Override
    public Integer executeOrder() {
        return 2;
    }
}
