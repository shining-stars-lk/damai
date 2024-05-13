package com.damai.exception;

import com.damai.util.StringUtil;
import com.damai.enums.BaseCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.damai.constant.GatewayConstant.CODE;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: code检查
 * @author: 阿星不是程序员
 **/
@Component
public class CheckCodeHandler {
    
    private final static String EXCEPTION_MESSAGE = "code参数为空";

    public void checkCode(String code){
        if (StringUtil.isEmpty(code)) {
            ArgumentError argumentError = new ArgumentError();
            argumentError.setArgumentName(CODE);
            argumentError.setMessage(EXCEPTION_MESSAGE);
            List<ArgumentError> argumentErrorList = new ArrayList<>();
            argumentErrorList.add(argumentError);
            throw new ArgumentException(BaseCode.ARGUMENT_EMPTY.getCode(),argumentErrorList);
        }
    }
}
