package com.example.filter;

import com.alibaba.fastjson.JSON;
import com.example.common.Result;
import com.example.core.StringUtil;
import com.example.enums.BaseCode;
import com.example.exception.ArgumentError;
import com.example.exception.ArgumentException;
import com.example.service.ChannelDataService;
import com.example.util.AesForClient;
import com.example.vo.GetChannelDataVo;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.example.constant.GatewayConstant.AES_FLAG;
import static com.example.constant.GatewayConstant.CODE;
import static com.example.constant.GatewayConstant.DEBUG;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * @program: toolkit
 * 参考 {@link org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory}
 * @author lk
 * @create: 2023-05-30
 */
@Component
@Slf4j
public class ResponseValidationFilter implements GlobalFilter, Ordered {
    
    @Value("${verify.switch:true}")
    private boolean verifySwitch;
    
    @Value("${aes.vector:default}")
    private String aesVector;
    
    @Autowired
    private ChannelDataService channelDataService;


    @Override
    public int getOrder() {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().response(decorate(exchange)).build());
    }

    /**
     * 解决netty buffer默认长度1024导致的接受body不全问题
     * @param exchange
     * @return
     */
    @SuppressWarnings("unchecked")
    private ServerHttpResponse decorate(ServerWebExchange exchange) {
        return new ServerHttpResponseDecorator(exchange.getResponse()) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                String originalResponseContentType = exchange
                        .getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE,
                        originalResponseContentType);

                ClientResponse clientResponse = ClientResponse
                        .create(exchange.getResponse().getStatusCode())
                        .headers(headers -> headers.putAll(httpHeaders))
                        .body(Flux.from(body)).build();

                //修改responseBody
                Mono<String> modifiedBody = clientResponse
                        .bodyToMono(String.class)
                        .flatMap(originalBody -> modifyResponseBody().apply(exchange,originalBody));

                BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody,
                        String.class);
                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(
                        exchange, exchange.getResponse().getHeaders());
                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            Flux<DataBuffer> messageBody = outputMessage.getBody();
                            HttpHeaders headers = getDelegate().getHeaders();
                            if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                messageBody = messageBody.doOnNext(data -> headers
                                        .setContentLength(data.readableByteCount()));
                            }
                            return getDelegate().writeWith(messageBody);
                        }));
            }

            /**
             * 修改responseBody
             * @return apply 返回Mono<String>，数据是修改后的responseBody
             */
            private BiFunction<ServerWebExchange, String, Mono<String>> modifyResponseBody() {
                return (serverWebExchange,responseBody) -> {
                    String modifyResponseBody = checkResponseBody(serverWebExchange, responseBody);
                    return Mono.just(modifyResponseBody);
                };
            }

            @Override
            public Mono<Void> writeAndFlushWith(
                    Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
    }

    private String checkResponseBody(final ServerWebExchange serverWebExchange, final String responseBody) {
        String modifyResponseBody = responseBody;
        ServerHttpRequest request = serverWebExchange.getRequest();
        String debug = request.getHeaders().getFirst(DEBUG);
        if (verifySwitch && (!StringUtil.isNotEmpty(debug) && "true".equals(debug))) {
            String aesFlag = request.getHeaders().getFirst(AES_FLAG);
            if (StringUtil.isNotEmpty(responseBody)) {
                Result<Object> result = JSON.parseObject(responseBody, Result.class);
                Object data = result.getData();
                if (data != null) {
                    String code = request.getHeaders().getFirst(CODE);
                    if (StringUtil.isEmpty(code)) {
                        ArgumentError argumentError = new ArgumentError();
                        argumentError.setArgumentName(CODE);
                        argumentError.setMessage("code参数为空");
                        List<ArgumentError> argumentErrorList = new ArrayList<>();
                        argumentErrorList.add(argumentError);
                        throw new ArgumentException(BaseCode.ARGUMENT_EMPTY.getCode(),argumentErrorList);
                    }
                    GetChannelDataVo channelDataVo = channelDataService.GetChannelDataByCode(code);
                    if (StringUtil.isNotEmpty(aesFlag) && "true".equals(aesFlag)) {
                        String encrypt = AesForClient.encrypt(channelDataVo.getAesKey(), aesVector, JSON.toJSONString(data));
                        result.setData(encrypt);
                        modifyResponseBody = JSON.toJSONString(result);
                    }
                }
            }
        }
        return modifyResponseBody;
    }
}
