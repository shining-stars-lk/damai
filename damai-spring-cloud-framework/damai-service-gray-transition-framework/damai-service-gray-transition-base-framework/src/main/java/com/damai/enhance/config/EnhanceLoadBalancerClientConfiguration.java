/*
 * Copyright 2012-2023 the original author or authors.
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

package com.damai.enhance.config;

import com.damai.balance.FilterLoadBalance;
import com.damai.enhance.EnhanceServiceInstanceListSupplierBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RetryAwareServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.XForwardedHeadersTransformer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.loadbalancer.support.LoadBalancerEnvironmentPropertyUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.RetrySpec;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 对 {@link LoadBalancerClientConfiguration} 的定制增强
 * @author: 阿星不是程序员
 **/
@AutoConfigureBefore(LoadBalancerClientConfiguration.class)
public class EnhanceLoadBalancerClientConfiguration {

	private static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 193827465;

	@Bean
	public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new RoundRobinLoadBalancer(
				loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnReactiveDiscoveryEnabled
	@Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER)
	public static class ReactiveSupportConfiguration {

		@Bean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@Conditional(DefaultConfigurationCondition.class)
		public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withCaching().build(context);
		}

		@Bean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@Conditional(ZonePreferenceConfigurationCondition.class)
		public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withCaching().withZonePreference()
					.build(context);
		}

		@Bean
		@ConditionalOnBean(LoadBalancerClientFactory.class)
		public XForwardedHeadersTransformer xForwarderHeadersTransformer(LoadBalancerClientFactory clientFactory) {
			return new XForwardedHeadersTransformer(clientFactory);
		}

		@Bean
		@ConditionalOnBean({ ReactiveDiscoveryClient.class, WebClient.Builder.class })
		@Conditional(HealthCheckConfigurationCondition.class)
		public ServiceInstanceListSupplier healthCheckDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withHealthChecks().build(context);
		}

		@Bean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@Conditional(RequestBasedStickySessionConfigurationCondition.class)
		public ServiceInstanceListSupplier requestBasedStickySessionDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withCaching()
					.withRequestBasedStickySession().build(context);
		}

		@Bean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@Conditional(SameInstancePreferenceConfigurationCondition.class)
		public ServiceInstanceListSupplier sameInstancePreferenceServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withCaching()
					.withSameInstancePreference().build(context);
		}

		@Bean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@Conditional(WeightedConfigurationCondition.class)
		public ServiceInstanceListSupplier weightedServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withCaching().withWeighted()
					.build(context);
		}

		@Bean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@Conditional(SubsetConfigurationCondition.class)
		public ServiceInstanceListSupplier subsetServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withDiscoveryClient().withSubset().withCaching()
					.build(context);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBlockingDiscoveryEnabled
	@Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER + 1)
	public static class BlockingSupportConfiguration {

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Conditional(DefaultConfigurationCondition.class)
		public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withCaching().build(context);
		}

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Conditional(ZonePreferenceConfigurationCondition.class)
		public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withCaching()
					.withZonePreference().build(context);
		}

		@Bean
		@ConditionalOnBean({ DiscoveryClient.class, RestTemplate.class })
		@Conditional(HealthCheckConfigurationCondition.class)
		public ServiceInstanceListSupplier healthCheckDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withBlockingHealthChecks()
					.build(context);
		}

		@Bean
		@ConditionalOnBean({ DiscoveryClient.class, RestClient.class })
		@Conditional(HealthCheckConfigurationCondition.class)
		public ServiceInstanceListSupplier healthCheckRestClientDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient()
					.withBlockingRestClientHealthChecks().build(context);
		}

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Conditional(RequestBasedStickySessionConfigurationCondition.class)
		public ServiceInstanceListSupplier requestBasedStickySessionDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withCaching()
					.withRequestBasedStickySession().build(context);
		}

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Conditional(SameInstancePreferenceConfigurationCondition.class)
		public ServiceInstanceListSupplier sameInstancePreferenceServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withCaching()
					.withSameInstancePreference().build(context);
		}

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Conditional(WeightedConfigurationCondition.class)
		public ServiceInstanceListSupplier weightedServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withCaching().withWeighted()
					.build(context);
		}

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Conditional(SubsetConfigurationCondition.class)
		public ServiceInstanceListSupplier subsetServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				FilterLoadBalance filterLoadBalance) {
			return new EnhanceServiceInstanceListSupplierBuilder(filterLoadBalance).withBlockingDiscoveryClient().withSubset().withCaching()
					.build(context);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBlockingDiscoveryEnabled
	@ConditionalOnClass(RetryTemplate.class)
	@Conditional(BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition.class)
	@AutoConfigureAfter(BlockingSupportConfiguration.class)
	@ConditionalOnBean(ServiceInstanceListSupplier.class)
	public static class BlockingRetryConfiguration {

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Primary
		public ServiceInstanceListSupplier retryAwareDiscoveryClientServiceInstanceListSupplier(
				ServiceInstanceListSupplier delegate) {
			return new RetryAwareServiceInstanceListSupplier(delegate);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnReactiveDiscoveryEnabled
	@Conditional(ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition.class)
	@AutoConfigureAfter(ReactiveSupportConfiguration.class)
	@ConditionalOnBean(ServiceInstanceListSupplier.class)
	@ConditionalOnClass(RetrySpec.class)
	public static class ReactiveRetryConfiguration {

		@Bean
		@ConditionalOnBean(DiscoveryClient.class)
		@Primary
		public ServiceInstanceListSupplier retryAwareDiscoveryClientServiceInstanceListSupplier(
				ServiceInstanceListSupplier delegate) {
			return new RetryAwareServiceInstanceListSupplier(delegate);
		}

	}

	static final class BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition extends AllNestedConditions {

		private BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true",
				matchIfMissing = true)
		static class LoadBalancerRetryEnabled {

		}

		@Conditional(AvoidPreviousInstanceEnabledCondition.class)
		static class AvoidPreviousInstanceEnabled {

		}

	}

	static final class ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition extends AllNestedConditions {

		private ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition() {
			super(ConfigurationPhase.REGISTER_BEAN);
		}

		@ConditionalOnProperty(value = "spring.cloud.loadbalancer.retry.enabled", havingValue = "true")
		static class LoadBalancerRetryEnabled {

		}

		@Conditional(AvoidPreviousInstanceEnabledCondition.class)
		static class AvoidPreviousInstanceEnabled {

		}

	}

	static class AvoidPreviousInstanceEnabledCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.trueOrMissingForClientOrDefault(context.getEnvironment(),
					"retry.avoid-previous-instance");
		}

	}

	static class DefaultConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToOrMissingForClientOrDefault(context.getEnvironment(),
					"configurations", "default");
		}

	}

	static class ZonePreferenceConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
					"configurations", "zone-preference");
		}

	}

	static class HealthCheckConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
					"configurations", "health-check");
		}

	}

	static class RequestBasedStickySessionConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
					"configurations", "request-based-sticky-session");
		}

	}

	static class SameInstancePreferenceConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
					"configurations", "same-instance-preference");
		}

	}

	static class WeightedConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
					"configurations", "weighted");
		}

	}

	static class SubsetConfigurationCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return LoadBalancerEnvironmentPropertyUtils.equalToForClientOrDefault(context.getEnvironment(),
					"configurations", "subset");
		}

	}

}
