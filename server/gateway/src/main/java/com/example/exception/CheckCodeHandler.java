package com.example.exception;

import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.constant.GatewayConstant.CODE;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-12-27
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
