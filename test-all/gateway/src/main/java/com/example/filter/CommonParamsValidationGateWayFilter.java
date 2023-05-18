package com.example.filter;


import com.alibaba.fastjson.JSON;
import com.example.conf.RequestWrapper;
import com.example.core.StringUtil;
import com.example.service.ChannelDataService;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.example.constant.Constant.MARK_PARAMETER;
import static com.example.constant.Constant.TRACE_ID;

/**
 * @program: gateway
 * @description:
 * @author: lk
 * @create: 2023-5-18
 **/
@Slf4j
@Component
public class CommonParamsValidationGateWayFilter implements GlobalFilter, Ordered {

    private static final String REQUEST_BODY = "body";

    private static final String CHARSET = "utf-8";

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    @Autowired
    private ChannelDataService channelDataService;

    @Value("${verify.switch:false}")
    private boolean verifySwitch;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_ID);
        String mark = request.getHeaders().getFirst(MARK_PARAMETER);
        if (StringUtil.isEmpty(traceId)) {
            traceId = "1111";
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
            requestWrapper.setMap(map);
            return Mono.just(body);
        });
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage,new BodyInserterContext()).then(Mono.defer(() ->
                chain.filter(exchange.mutate().request(decorate(exchange, headers, outputMessage, requestWrapper, traceId)).build())
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
        if (verifySwitch) {
            //应用渠道
            String code = requestBodyContent.get("code");
            GetChannelDataVo getChannelDataVo = channelDataService.GetChannelDataByCode(code);
            boolean checkFlag = SignatureUtil.rsa256Check(requestBodyContent, getChannelDataVo.getPublicKey(), CHARSET);
        }
        Map<String,String> map = new HashMap<>(4);
        map.put(REQUEST_BODY,requestBody);
        return map;
    }
    //将网关层request请求头中的重要参数传递给后续的微服务中
    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage, RequestWrapper requestWrapper, String traceId){
        return new ServerHttpRequestDecorator(exchange.getRequest()){
            @Override
            public HttpHeaders getHeaders() {
                log.info("current thread getHeaders: {}",Thread.currentThread().getName());
                long contentLength = headers.getContentLength();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.putAll(headers);
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
        return 1;
    }



    public static String getMsg(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        String string = sw.toString();
        try {
            sw.close();
            pw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return string;

    }
}
