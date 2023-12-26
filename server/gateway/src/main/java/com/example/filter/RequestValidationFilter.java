package com.example.filter;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.example.conf.RequestTemporaryWrapper;
import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.ArgumentError;
import com.example.exception.ArgumentException;
import com.example.exception.CookFrameException;
import com.example.property.GatewayProperty;
import com.example.service.ApiRestrictService;
import com.example.service.ChannelDataService;
import com.example.service.TokenService;
import com.example.threadlocal.BaseParameterHolder;
import com.example.util.RSATool;
import com.example.util.RsaSignTool;
import com.example.vo.GetChannelDataVo;
import com.example.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
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
import static com.example.constant.GatewayConstant.BUSINESS_BODY;
import static com.example.constant.GatewayConstant.CODE;
import static com.example.constant.GatewayConstant.ENCRYPT;
import static com.example.constant.GatewayConstant.NO_VERIFY;
import static com.example.constant.GatewayConstant.REQUEST_BODY;
import static com.example.constant.GatewayConstant.TOKEN;
import static com.example.constant.GatewayConstant.VERIFY_VALUE;

/**
 * @program: gateway
 * @description:
 * @author: 星哥
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
    private ApiRestrictService apiRestrictService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private GatewayProperty gatewayProperty;
    @Resource
    private UidGenerator uidGenerator;
    

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_ID);
        String mark = request.getHeaders().getFirst(MARK_PARAMETER);
        String noVerify = request.getHeaders().getFirst(NO_VERIFY);
        if (StringUtil.isEmpty(traceId)) {
            traceId = String.valueOf(uidGenerator.getUID());
        }
        MDC.put(TRACE_ID,traceId);
        Map<String,String> headMap = new HashMap<>();
        headMap.put(TRACE_ID,traceId);
        headMap.put(MARK_PARAMETER,mark);
        if (StringUtil.isNotEmpty(noVerify)) {
            headMap.put(NO_VERIFY,noVerify);
        }
        BaseParameterHolder.setParameter(TRACE_ID,traceId);
        BaseParameterHolder.setParameter(MARK_PARAMETER,mark);
        MediaType contentType = request.getHeaders().getContentType();
        //application json请求
        if (contentType != null && contentType.toString().toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE.toLowerCase())) {
            return readBody(exchange,chain,headMap);
        }else {
            Map<String, String> map = doExecute("", exchange);
            map.remove(REQUEST_BODY);
            map.putAll(headMap);
            request.mutate().headers(httpHeaders -> {
                map.forEach(httpHeaders::add);
            });
            return chain.filter(exchange);
        }
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, Map<String,String> headMap){
        log.info("current thread readBody : {}",Thread.currentThread().getName());
        RequestTemporaryWrapper requestTemporaryWrapper = new RequestTemporaryWrapper();
        
        ServerRequest serverRequest = ServerRequest.create(exchange, serverCodecConfigurer.getReaders());
        Mono<String> modifiedBody = serverRequest
                .bodyToMono(String.class)
                .flatMap(originalBody -> Mono.just(execute(requestTemporaryWrapper,originalBody,exchange)))
                .switchIfEmpty(Mono.defer(() -> Mono.just(execute(requestTemporaryWrapper,"",exchange))));
        
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter
                .insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> chain.filter(
                        exchange.mutate().request(decorateHead(exchange, headers, outputMessage, requestTemporaryWrapper, headMap)).build()
                )))
                .onErrorResume((Function<Throwable, Mono<Void>>) throwable -> Mono.error(throwable));
    }
    
    public String execute(RequestTemporaryWrapper requestTemporaryWrapper,String requestBody,ServerWebExchange exchange){
        //进行业务验证，并将相关参数放入map
        Map<String, String> map = doExecute(requestBody, exchange);
        String body = map.get(REQUEST_BODY);
        map.remove(REQUEST_BODY);
        requestTemporaryWrapper.setMap(map);
        return body;
    }

    private Map<String,String> doExecute(String originalBody,ServerWebExchange exchange){
        log.info("current thread verify: {}",Thread.currentThread().getName());
        ServerHttpRequest request = exchange.getRequest();
        String requestBody = originalBody;
        Map<String, String> bodyContent = new HashMap<>();
        if (StringUtil.isNotEmpty(originalBody)) {
            bodyContent = JSON.parseObject(originalBody, Map.class);
        }
        String code = null;
        String token;   
        String userId = null;
        String url = request.getPath().value();
        String noVerify = request.getHeaders().getFirst(NO_VERIFY);
        if (!(StringUtil.isNotEmpty(noVerify) && VERIFY_VALUE.equals(noVerify))) {

            String encrypt = request.getHeaders().getFirst(ENCRYPT);
            //应用渠道
            code = bodyContent.get(CODE);
            //token
            token = request.getHeaders().getFirst(TOKEN);

            if (StringUtil.isEmpty(code)) {
                ArgumentError argumentError = new ArgumentError();
                argumentError.setArgumentName(CODE);
                argumentError.setMessage("code参数为空");
                List<ArgumentError> argumentErrorList = new ArrayList<>();
                argumentErrorList.add(argumentError);
                throw new ArgumentException(BaseCode.ARGUMENT_EMPTY.getCode(),argumentErrorList);
            }
            
            GetChannelDataVo channelDataVo = channelDataService.getChannelDataByCode(code);
            
            if (StringUtil.isNotEmpty(encrypt) && "v2".equals(encrypt)) {
                String decrypt = RSATool.decrypt(bodyContent.get(BUSINESS_BODY),channelDataVo.getDataSecretKey());
                bodyContent.put(BUSINESS_BODY,decrypt);
            }
            SecureUtil.sign(SignAlgorithm.SHA256withRSA);
            boolean checkFlag = RsaSignTool.verifyRsaSign256(bodyContent, channelDataVo.getSignPublicKey());
            if (!checkFlag) {
                throw new CookFrameException(BaseCode.CHANNEL_DATA);
            }

            boolean skipCheckTokenResult = skipCheckToken(url);
            if (!skipCheckTokenResult && StringUtil.isEmpty(token)) {
                ArgumentError argumentError = new ArgumentError();
                argumentError.setArgumentName(token);
                argumentError.setMessage("token参数为空");
                List<ArgumentError> argumentErrorList = new ArrayList<>();
                argumentErrorList.add(argumentError);
                throw new ArgumentException(BaseCode.ARGUMENT_EMPTY.getCode(),argumentErrorList);
            }

            if (!skipCheckTokenResult) {
                UserVo userVo = tokenService.getUser(token);
                userId = userVo.getId();
            }
            
            requestBody = bodyContent.get(BUSINESS_BODY);
        }
        apiRestrictService.apiRestrict(userId,url,request);
        Map<String,String> map = new HashMap<>(4);
        map.put(REQUEST_BODY,requestBody);
        if (StringUtil.isNotEmpty(code)) {
            map.put(CODE,code);
        }
        return map;
    }
    //将网关层request请求头中的重要参数传递给后续的微服务中
    private ServerHttpRequestDecorator decorateHead(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage, RequestTemporaryWrapper requestTemporaryWrapper, Map<String,String> headMap){
        return new ServerHttpRequestDecorator(exchange.getRequest()){
            @Override
            public HttpHeaders getHeaders() {
                log.info("current thread getHeaders: {}",Thread.currentThread().getName());
                long contentLength = headers.getContentLength();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.putAll(headers);
                Map<String, String> map = requestTemporaryWrapper.getMap();
                if (map != null) {
                    newHeaders.setAll(map);
                }
                if (headMap != null) {
                    newHeaders.setAll(headMap);
                }
                if (contentLength > 0){
                    newHeaders.setContentLength(contentLength);
                }else {
                    newHeaders.set(HttpHeaders.TRANSFER_ENCODING,"chunked");
                }
                if (headMap != null && StringUtil.isNotEmpty(headMap.get(TRACE_ID))) {
                    MDC.put(TRACE_ID,headMap.get(TRACE_ID));
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
        return -2;
    }

    public boolean skipCheckToken(String url){
        if (gatewayProperty.getSkipCheckTokenPaths() != null) {
            for (String skipCheckTokenPath : gatewayProperty.getSkipCheckTokenPaths()) {
                PathMatcher matcher = new AntPathMatcher();
                if(matcher.match(skipCheckTokenPath, url)){
                    return true;
                }
            }
        }
        return false;
    }
}
