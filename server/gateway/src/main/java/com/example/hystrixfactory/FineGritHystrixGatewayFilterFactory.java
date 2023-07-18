/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.hystrixfactory;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;
import rx.Subscription;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.containsEncodedParts;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.removeAlreadyRouted;

/**
 * Depends on `spring-cloud-starter-netflix-hystrix`,
 * {@see https://cloud.spring.io/spring-cloud-netflix/}.
 *
 * @author Spencer Gibb
 * @author Michele Mancioppi
 * @author Olga Maciaszek-Sharma
 */
public class FineGritHystrixGatewayFilterFactory
		extends AbstractGatewayFilterFactory<FineGritHystrixGatewayFilterFactory.Config> {

	private final ObjectProvider<DispatcherHandler> dispatcherHandlerProvider;

	// do not use this dispatcherHandler directly, use getDispatcherHandler() instead.
	private volatile DispatcherHandler dispatcherHandler;

	public FineGritHystrixGatewayFilterFactory(
			ObjectProvider<DispatcherHandler> dispatcherHandlerProvider) {
		super(Config.class);
		this.dispatcherHandlerProvider = dispatcherHandlerProvider;
	}

	private DispatcherHandler getDispatcherHandler() {
		if (dispatcherHandler == null) {
			dispatcherHandler = dispatcherHandlerProvider.getIfAvailable();
		}

		return dispatcherHandler;
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return singletonList(NAME_KEY);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange,
					GatewayFilterChain chain) {
				return Mono.deferWithContext(context -> {
					
					ServerHttpRequest request = exchange.getRequest();
					String path = request.getPath().pathWithinApplication().value();
					Map<String, Integer> rulMap = config.getUrlMap();
					Integer timeout = null;
					if (rulMap != null) {
						timeout = rulMap.get(path);
					}
					
					RouteHystrixCommand command;
					if (timeout == null) {
						//没有额外配置url级别的超时时间则用默认时间
						command = new RouteHystrixCommand(config, exchange, chain, path);
					} else {
						//有配置时间的接口将使用配置的时间
						command = new RouteHystrixCommand(config, exchange, chain, timeout, path);
					}
					return Mono.create(s -> {
						Subscription sub = command.toObservable().subscribe(s::success,
								s::error, s::success);
						s.onCancel(sub::unsubscribe);
					}).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> {
						if (throwable instanceof HystrixRuntimeException) {
							HystrixRuntimeException e = (HystrixRuntimeException) throwable;
							HystrixRuntimeException.FailureType failureType = e
									.getFailureType();

							switch (failureType) {
							case TIMEOUT:
								return Mono.error(new TimeoutException());
							case SHORTCIRCUIT:
								return Mono.error(new ServiceUnavailableException());
							case COMMAND_EXCEPTION: {
								Throwable cause = e.getCause();

								/*
								 * We forsake here the null check for cause as
								 * HystrixRuntimeException will always have a cause if the
								 * failure type is COMMAND_EXCEPTION.
								 */
								if (cause instanceof ResponseStatusException
										|| AnnotatedElementUtils.findMergedAnnotation(
												cause.getClass(),
												ResponseStatus.class) != null) {
									return Mono.error(cause);
								}
							}
							default:
								break;
							}
						}
						return Mono.error(throwable);
					}).then();
				});
			}

			@Override
			public String toString() {
				return filterToStringCreator(FineGritHystrixGatewayFilterFactory.this)
						.append("id", config.getId())
						.append("fallback", config.fallbackUri).toString();
			}
		};
	}
	
	@Override
	public String name() {
		return "FineGritHystrix";
	}

	public static class Config {
		
		private String id;

		private URI fallbackUri;
		
		/**
		 * key url
		 * value timeout ms
		 */
		private Map<String, Integer> urlMap;
		
		public String getId() {
			return id;
		}
		
		public Config setId(String id) {
			this.id = id;
			return this;
		}

		public Config setFallbackUri(String fallbackUri) {
			if (fallbackUri != null) {
				setFallbackUri(URI.create(fallbackUri));
			}
			return this;
		}

		public URI getFallbackUri() {
			return fallbackUri;
		}

		public void setFallbackUri(URI fallbackUri) {
			if (fallbackUri != null && !"forward".equals(fallbackUri.getScheme())) {
				throw new IllegalArgumentException(
						"Hystrix Filter currently only supports 'forward' URIs, found "
								+ fallbackUri);
			}
			this.fallbackUri = fallbackUri;
		}
		
		public Map<String, Integer> getUrlMap() {
			return urlMap;
		}
		
		public Config setUrlMap(Map<String, Integer> timeout) {
			Map<String, Integer> map = new HashMap<>(timeout.size());
			for (String key : timeout.keySet()) {
				Integer value = timeout.get(key);
				
				key = key.replace("-", "/");
				if (!key.startsWith("/")) {
					key = "/" + key;
				}
				if (key.endsWith("/")) {
					key = key + "**";
				}
				map.put(key, value);
			}
			this.urlMap = map;
			return this;
		}
	}

	// TODO: replace with HystrixMonoCommand that we write
	private class RouteHystrixCommand extends HystrixObservableCommand<Void> {

		private final URI fallbackUri;

		private final ServerWebExchange exchange;

		private final GatewayFilterChain chain;

		RouteHystrixCommand(Config config, ServerWebExchange exchange,
				GatewayFilterChain chain, String hystrixKey) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(hystrixKey))
					.andCommandKey(HystrixCommandKey.Factory.asKey(config.id)));
			this.fallbackUri = config.getFallbackUri();
			this.exchange = exchange;
			this.chain = chain;
		}
		
		RouteHystrixCommand(Config config, ServerWebExchange exchange,
							GatewayFilterChain chain, int timeout, String hystrixKey) {
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(hystrixKey))
					.andCommandKey(HystrixCommandKey.Factory.asKey(config.id))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(timeout)));
			this.fallbackUri = config.getFallbackUri();
			this.exchange = exchange;
			this.chain = chain;
		}

		@Override
		protected Observable<Void> construct() {
			return RxReactiveStreams
					.toObservable(this.chain.filter(exchange));
		}

		@Override
		protected Observable<Void> resumeWithFallback() {
			if (this.fallbackUri == null) {
				return super.resumeWithFallback();
			}

			// TODO: copied from RouteToRequestUrlFilter
			URI uri = exchange.getRequest().getURI();
			// TODO: assume always?
			boolean encoded = containsEncodedParts(uri);
			URI requestUrl = UriComponentsBuilder.fromUri(uri).host(null).port(null)
					.uri(this.fallbackUri).scheme(null).build(encoded).toUri();
			exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
			addExceptionDetails();

			ServerHttpRequest request = this.exchange.getRequest().mutate()
					.uri(requestUrl).build();
			ServerWebExchange mutated = exchange.mutate().request(request).build();
			// Before we continue on remove the already routed attribute since the
			// fallback may go back through the route handler if the fallback
			// is to another route in the Gateway
			removeAlreadyRouted(mutated);
			return RxReactiveStreams.toObservable(getDispatcherHandler().handle(mutated));
		}

		private void addExceptionDetails() {
			Throwable executionException = getExecutionException();
			ofNullable(executionException).ifPresent(exception -> exchange.getAttributes()
					.put(HYSTRIX_EXECUTION_EXCEPTION_ATTR, exception));
		}

	}

}
