package com.example.filter;


import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.example.conf.RequestTemporaryWrapper;
import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.ArgumentError;
import com.example.exception.ArgumentException;
import com.example.exception.ToolkitException;
import com.example.property.GatewayProperty;
import com.example.service.ApiRestrictService;
import com.example.service.ChannelDataService;
import com.example.service.TokenService;
import com.example.threadlocal.BaseParameterHolder;
import com.example.util.AesForClient;
import com.example.util.SignUtil;
import com.example.vo.GetChannelDataVo;
import com.example.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
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
import static com.example.constant.GatewayConstant.CHARSET;
import static com.example.constant.GatewayConstant.CODE;
import static com.example.constant.GatewayConstant.DEBUG;
import static com.example.constant.GatewayConstant.ENCRYPT;
import static com.example.constant.GatewayConstant.REQUEST_BODY;
import static com.example.constant.GatewayConstant.TOKEN;

/**
 * @program: gateway
 * @description:
 * @author: k
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

    @Value("${aes.vector:default}")
    private String aesVector;



    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_ID);
        String mark = request.getHeaders().getFirst(MARK_PARAMETER);
        String debug = request.getHeaders().getFirst(DEBUG);
        if (StringUtil.isEmpty(traceId)) {
            traceId = String.valueOf(uidGenerator.getUID());
        }
        MDC.put(TRACE_ID,traceId);
        Map<String,String> headMap = new HashMap<>();
        headMap.put(TRACE_ID,traceId);
        headMap.put(MARK_PARAMETER,mark);
        if (StringUtil.isNotEmpty(debug)) {
            headMap.put(DEBUG,debug);
        }
        BaseParameterHolder.setParameter(TRACE_ID,traceId);
        BaseParameterHolder.setParameter(MARK_PARAMETER,mark);
        //if (HttpMethod.POST.equals(request.getMethod()) || HttpMethod.PUT.equals(request.getMethod())) {
            //MediaType contentType = request.getHeaders().getContentType();
            //if (MediaType.APPLICATION_JSON.equals(contentType)){
                //application json请求
                return readBody(exchange,chain,headMap);
            //}
        //}
        //return chain.filter(exchange);
    }

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, Map<String,String> headMap){
        log.info("current thread readBody : {}",Thread.currentThread().getName());
        RequestTemporaryWrapper requestTemporaryWrapper = new RequestTemporaryWrapper();

        ServerRequest serverRequest = ServerRequest.create(exchange, serverCodecConfigurer.getReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(originalBody -> {
            //进行业务验证，并将相关参数放入map
            Map<String, String> map = verify(originalBody, exchange);
            String body = map.get(REQUEST_BODY);
            map.remove(REQUEST_BODY);
            requestTemporaryWrapper.setMap(map);
            return Mono.just(body);
        });
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

    private Map<String,String> verify(String originalBody,ServerWebExchange exchange){
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
        String debug = request.getHeaders().getFirst(DEBUG);
        if (!(StringUtil.isNotEmpty(debug) && "true".equals(debug))) {

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

            if (StringUtil.isEmpty(encrypt)) {
                ArgumentError argumentError = new ArgumentError();
                argumentError.setArgumentName(encrypt);
                argumentError.setMessage("encrypt请求头参数为空");
                List<ArgumentError> argumentErrorList = new ArrayList<>();
                argumentErrorList.add(argumentError);
                throw new ArgumentException(BaseCode.HEAD_ARGUMENT_EMPTY.getCode(),argumentErrorList);
            }
            GetChannelDataVo channelDataVo = channelDataService.getChannelDataByCode(code);
            boolean checkFlag = SignUtil.rsa256Check(bodyContent, channelDataVo.getPublicKey(), CHARSET);
            if (!checkFlag) {
                throw new ToolkitException(BaseCode.CHANNEL_DATA);
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

            if (StringUtil.isNotEmpty(encrypt) && "v2".equals(encrypt)) {
                String dec = AesForClient.decrypt(channelDataVo.getAesKey(), aesVector, bodyContent.get(BUSINESS_BODY));
                requestBody = dec;
            }else {
                requestBody = bodyContent.get(BUSINESS_BODY);
            }
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
        if (gatewayProperty.getSkipCheckTokenPaths() != null && gatewayProperty.getSkipCheckTokenPaths().length > 0) {
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
