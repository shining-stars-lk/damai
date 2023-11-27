package com.example.exception;

import com.example.common.ApiResponse;
import com.example.enums.BaseCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    /*
    * 业务异常
    * */
    @ExceptionHandler(value = CookFrameException.class)
    public ApiResponse toolkitExceptionHandler(HttpServletRequest request, CookFrameException cookFrameException) {
        log.error("业务异常 method : {} url : {} query : {} ", request.getMethod(), getRequestUrl(request), getRequestQuery(request), cookFrameException);
        return ApiResponse.error(cookFrameException.getCode(), cookFrameException.getMessage());
    }
    /**
     * 参数验证异常
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error("参数验证异常 method : {} url : {} query : {} ", request.getMethod(), getRequestUrl(request), getRequestQuery(request), ex);
        BindingResult bindingResult = ex.getBindingResult();
        List<ArgumentError> argumentErrorList = 
                bindingResult.getFieldErrors()
                        .stream()
                        .map(fieldError -> {
                            ArgumentError argumentError = new ArgumentError();
                            argumentError.setArgumentName(fieldError.getField());
                            argumentError.setMessage(fieldError.getDefaultMessage());
                            return argumentError;
                        }).collect(Collectors.toList());
        return ApiResponse.error(BaseCode.PARAMETER_ERROR.getCode(),argumentErrorList);
    }

    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Throwable.class)
    public ApiResponse defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("全局异常 method : {} url : {} query : {} ", request.getMethod(), getRequestUrl(request), getRequestQuery(request), throwable);
        return ApiResponse.error();
    }

    private String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    private String getRequestQuery(HttpServletRequest request){
        return request.getQueryString();
    }
}
