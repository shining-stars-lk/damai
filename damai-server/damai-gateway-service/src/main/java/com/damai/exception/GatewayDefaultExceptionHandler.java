package com.damai.exception;

import com.damai.common.ApiResponse;
import com.damai.conf.RequestTemporaryWrapper;
import com.damai.enums.BaseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 自定义异常
 * @author: 阿星不是程序员
 **/
@Slf4j
public class GatewayDefaultExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        //1.获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //2.response是否结束  用于多个异常处理时候
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        //2.设置响应头类型
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //3.设置响应状态吗
        boolean exceptionFlag = false;
        RequestTemporaryWrapper requestTemporaryWrapper = new RequestTemporaryWrapper();
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException)ex;
            if (responseStatusException.getStatusCode() == HttpStatus.NOT_FOUND) {
                String path = exchange.getRequest().getPath().value();
                String methodValue = exchange.getRequest().getMethod().name();
                ApiResponse.error(BaseCode.NOT_FOUND.getCode(),String.format(BaseCode.NOT_FOUND.getMsg(),methodValue,path));
            }
        }else if (ex instanceof DaMaiFrameException) {
            DaMaiFrameException daMaiFrameException = (DaMaiFrameException)ex;
            ApiResponse<String> apiResponse = ApiResponse.error(daMaiFrameException.getCode(), daMaiFrameException.getMessage());
            requestTemporaryWrapper.setApiResponse(apiResponse);
            exceptionFlag = true;
        }else if (ex instanceof ArgumentException) {
            ArgumentException ae = (ArgumentException)ex;
            ApiResponse<Object> apiResponse = ApiResponse.error(ae.getCode(), ae.getMessage());
            apiResponse.setData(ae.getArgumentErrorList());
            requestTemporaryWrapper.setApiResponse(apiResponse);
            exceptionFlag = true;
        }else if (ex instanceof Exception) {
            ApiResponse<String> apiResponse = ApiResponse.error(-100,"网络异常!");
            requestTemporaryWrapper.setApiResponse(apiResponse);
            exceptionFlag = true;
        }
        if (exceptionFlag) {
            response.setStatusCode(HttpStatus.OK);
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //4.设置响应内容
        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        //设置响应到response的数据
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(requestTemporaryWrapper.getApiResponse()));
                    } catch (JsonProcessingException e) {
                        log.error("response error",e);
                        return null;
                    }
                }));
    }
}
