package com.example.filter;


import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.example.conf.RequestWrapper;
import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.ArgumentError;
import com.example.exception.ArgumentException;
import com.example.exception.ToolkitException;
import com.example.service.ApiLimitService;
import com.example.service.ChannelDataService;
import com.example.util.AesForClient;
import com.example.util.SignatureUtil;
import com.example.vo.GetChannelDataVo;
import com.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.example.constant.Constant.MARK_PARAMETER;
import static com.example.constant.Constant.TRACE_ID;
import static com.example.constant.GatewayConstant.AES_FLAG;
import static com.example.constant.GatewayConstant.BUSINESS_BODY;
import static com.example.constant.GatewayConstant.CHARSET;
import static com.example.constant.GatewayConstant.CODE;
import static com.example.constant.GatewayConstant.REQUEST_BODY;
import static com.example.constant.GatewayConstant.RSA_FLAG;

/**
 * @program: gateway
 * @description:
 * @author: lk
 * @create: 2023-5-18
 **/

@Component
@Slf4j
public class RequestValidationFilter implements GlobalFilter, Ordered {

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    @Autowired
    private ChannelDataService channelDataService;
    
    @Autowired
    private ApiLimitService apiLimitService;
    
    @Resource
    private UidGenerator uidGenerator;

    @Value("${verify.switch:false}")
    private boolean verifySwitch;
    
    @Value("${aes.vector}")
    private String aesVector;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_ID);
        String mark = request.getHeaders().getFirst(MARK_PARAMETER);
        if (StringUtil.isEmpty(traceId)) {
            traceId = String.valueOf(uidGenerator.getUID());
        }
        if (StringUtil.isEmpty(BaseParameterHolder.getParameter(TRACE_ID))) {
            BaseParameterHolder.setParameter(TRACE_ID,traceId);
        }
        if (StringUtil.isEmpty(BaseParameterHolder.getParameter(MARK_PARAMETER))) {
            BaseParameterHolder.setParameter(MARK_PARAMETER,mark);
        }
        //if (HttpMethod.POST.equals(request.getMethod()) || HttpMethod.PUT.equals(request.getMethod())) {
            MediaType contentType = request.getHeaders().getContentType();
            if (MediaType.APPLICATION_JSON.equals(contentType)){
                //application json请求
                return readBody(exchange,chain,TRACE_ID);
            }
        //}
        return chain.filter(exchange);
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, String traceId){
        log.info("current thread readBody : {}",Thread.currentThread().getName());
        RequestWrapper requestWrapper = new RequestWrapper();

        ServerRequest serverRequest = ServerRequest.create(exchange, serverCodecConfigurer.getReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(originalBody -> {
            //进行业务验证，并将相关参数放入map
            Map<String, String> map = verify(originalBody, exchange);
            String body = map.get(REQUEST_BODY);
            map.remove(REQUEST_BODY);
            requestWrapper.setMap(map);
            return Mono.just(body);
        });
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage,new BodyInserterContext()).then(Mono.defer(() ->
                chain.filter(exchange.mutate().request(decorateHead(exchange, headers, outputMessage, requestWrapper, traceId)).build())
        )).onErrorResume((Function<Throwable,Mono<Void>>) throwable -> Mono.error(throwable));
    }

    private Map<String,String> verify(String originalBody,ServerWebExchange exchange){
        log.info("current thread verify: {}",Thread.currentThread().getName());
        ServerHttpRequest request = exchange.getRequest();
        String requestBody = originalBody;
        Map<String, String> requestBodyContent = new HashMap<>();
        if (StringUtil.isNotEmpty(originalBody)) {
            requestBodyContent = JSON.parseObject(originalBody, Map.class);
        }
        String code = null;
        if (verifySwitch) {
            String requestURI = request.getPath().value();
            String aesFlag = request.getHeaders().getFirst(AES_FLAG);
            String rsaFlag = request.getHeaders().getFirst(RSA_FLAG);
            //应用渠道
            code = requestBodyContent.get(CODE);
            if (StringUtil.isEmpty(code)) {
                ArgumentError argumentError = new ArgumentError();
                argumentError.setArgumentName(CODE);
                argumentError.setMessage("code参数为空");
                List<ArgumentError> argumentErrorList = new ArrayList<>();
                argumentErrorList.add(argumentError);
                throw new ArgumentException(BaseCode.ARGUMENT_EMPTY.getCode(),argumentErrorList);
            }
            GetChannelDataVo channelDataVo = channelDataService.GetChannelDataByCode(code);
            if (StringUtil.isNotEmpty(rsaFlag) && "true".equals(rsaFlag)) {
                boolean checkFlag = SignatureUtil.rsa256Check(requestBodyContent, channelDataVo.getPublicKey(), CHARSET);
                if (!checkFlag) {
                    throw new ToolkitException(BaseCode.CHANNEL_DATA);
                }
            }
            if (StringUtil.isNotEmpty(aesFlag) && "true".equals(aesFlag)) {
                String dec = AesForClient.decrypt(channelDataVo.getAesKey(), aesVector, requestBodyContent.get(BUSINESS_BODY));
                requestBody = dec;
            }
            apiLimitService.apiLimit("1",requestURI,request);
        }
        Map<String,String> map = new HashMap<>(4);
        map.put(REQUEST_BODY,requestBody);
        if (StringUtil.isNotEmpty(code)) {
            map.put(CODE,code);
        }
        return map;
    }
    //将网关层request请求头中的重要参数传递给后续的微服务中
    private ServerHttpRequestDecorator decorateHead(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage, RequestWrapper requestWrapper, String traceId){
        return new ServerHttpRequestDecorator(exchange.getRequest()){
            @Override
            public HttpHeaders getHeaders() {
                log.info("current thread getHeaders: {}",Thread.currentThread().getName());
                long contentLength = headers.getContentLength();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.putAll(headers);
                Map<String, String> map = requestWrapper.getMap();
                newHeaders.setAll(map);
                newHeaders.set(TRACE_ID,traceId);
                if (contentLength > 0){
                    newHeaders.setContentLength(contentLength);
                }else {
                    newHeaders.set(HttpHeaders.TRANSFER_ENCODING,"chunked");
                }
                return newHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
