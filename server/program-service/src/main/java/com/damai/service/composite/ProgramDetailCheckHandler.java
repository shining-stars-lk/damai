package com.damai.service.composite;

import com.damai.composite.AbstractComposite;
import com.damai.dto.ProgramGetDto;
import com.damai.dto.ProgramOrderCreateDto;
import com.damai.enums.CompositeCheckType;
import com.damai.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目检查
 * @author: 阿宽不是程序员
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
