package com.example.exception;

import com.example.common.ApiResponse;
import com.example.conf.RequestTemporaryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: 
 * @description:
 * @author: 星哥
 * @create: 2023-04-27
 **/
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

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
        if (ex instanceof ToolkitException) {
            ToolkitException toolkitException = (ToolkitException)ex;
            ApiResponse apiResponse = ApiResponse.error(toolkitException.getCode(),toolkitException.getMessage());
            requestTemporaryWrapper.setApiResponse(apiResponse);
            exceptionFlag = true;
        }else if (ex instanceof ArgumentException) {
            ArgumentException ae = (ArgumentException)ex;
            ApiResponse<Object> apiResponse = ApiResponse.error(ae.getCode(), ae.getMessage());
            apiResponse.setData(ae.getArgumentErrorList());
            requestTemporaryWrapper.setApiResponse(apiResponse);
            exceptionFlag = true;
        }else if (ex instanceof Exception) {
            ApiResponse apiResponse = ApiResponse.error(-100,"网络异常!");
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
                        e.printStackTrace();
                        return null;
                    }
                }));
    }
}
