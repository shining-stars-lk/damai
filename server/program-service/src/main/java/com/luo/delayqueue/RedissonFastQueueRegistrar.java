package com.luo.delayqueue;

import com.luo.delayqueue.context.DelayQueueComponent;
import com.luo.delayqueue.context.FastDelayQueueContext;
import com.luo.delayqueue.context.RedissonClientProperties;
import com.luo.delayqueue.tools.PackageScans;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedissonFastQueueRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, EnvironmentAware {
	private Environment environment;
	private BeanFactory beanFactory;
	private BeanDefinitionRegistry registry;
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		this.registry = registry;
		Map<String, Object> mapper = importingClassMetadata.getAnnotationAttributes(EnableRedissonFastDelayQueue.class.getName());
		int pollEventNumber = (int) mapper.get("poll");
		int partition = (int) mapper.get("partition");
		
		if (pollEventNumber <= 0 || partition <= 0) {
			throw new RuntimeException("配置值有错误! EnableRedissonDelayFastQueue注解的pollEventNumber,partition字段 必须大于0");
		}
		// 扫描有多少个 客户端
		Set<Class<?>> clientClasses = findClient();
		// init
		init(clientClasses, pollEventNumber, partition);
		
	}
	
	private void init(Set<Class<?>> clientClasses, int pollEventNumber, int partition) {
		for (Class<?> clientClass : clientClasses) {
			RedissonFastDelayQueueClient client = clientClass.getAnnotation(RedissonFastDelayQueueClient.class);
			String businessKey = StringUtils.isEmpty(client.businessKey()) ? clientClass.getName() + "_redisson_delay_queue" : client.businessKey();
			
			RedissonClientProperties properties = new RedissonClientProperties(getRedissonClient(), businessKey, beanFactory);
			
			properties.addPollEventNumberConfigure(businessKey, client.pollEventNumber() <= 0 ? pollEventNumber : client.pollEventNumber());
			
			properties.put(RedissonClientProperties.PartitionsKey, client.partition() <= 0 ? partition : client.partition());
			// 设置 客户端class
			properties.setClientClass(clientClass);
			// 解析 触发结果接收方法
			properties.setInvokeTrigger(parseDelayTriggerMethod(clientClass));
			// 将客户端 注入spring
			registry.registerBeanDefinition(businessKey, new RootBeanDefinition(clientClass));
			// 将客户端对应的 队列 存入 环境
			FastDelayQueueContext.putContext(clientClass, businessKey, new DelayQueueComponent(properties));
		}
	}
	
	private Method parseDelayTriggerMethod(Class<?> clientClass) {
		for (Method method : clientClass.getDeclaredMethods()) {
			if (!method.isAnnotationPresent(DelayTrigger.class)) {
				continue;
			}
			method.setAccessible(true);
			return method;
		}
		throw new NullPointerException(clientClass.getName() + " 必须指定一个方法作为接收延时触发回调，请在回调方法上配置@DelayTrigger注解");
	}
	
	private RedissonClient getRedissonClient() {
		RedissonClient client = null;
		try {
			// from spring
			client = beanFactory.getBean(RedissonClient.class);
			if (null != client) {
				return client;
			}
		} catch (Exception e) {
		}
		String pwd = environment.getProperty("spring.redis.password","qaz123");
		pwd = "qaz123";
		int port = Integer.parseInt(environment.getProperty("spring.redis.port", "6379"));
		if (environment.containsProperty("spring.redis.cluster.nodes")) {
			// 集群
			String nodes = environment.getProperty("spring.redis.cluster.nodes", "127.0.0.1:6379");
			Config config = new Config();
			// redisson版本是3.5，集群的imp前面要加上“redis://”，不然会报错，3.2版本可不加
			String[] addrs = nodes.split(",");
			if (addrs == null || addrs.length == 0) {
				addrs = nodes.split(";");
			}
			if (addrs == null || addrs.length == 0) {
				throw new IllegalArgumentException("redis集群地址不正确:" + nodes);
			}
			List<String> clusterNodes = new ArrayList<>();
			for (int i = 0; i < addrs.length; i++) {
				clusterNodes.add("redis://" + addrs[i]);
			}
			// 添加集群地址
			ClusterServersConfig clusterServersConfig = config.useClusterServers().addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
			// 设置密码
			clusterServersConfig.setPassword(pwd);
			return Redisson.create(config);
		}
		String host = environment.getProperty("spring.redis.host", "www.cookframe.com");
		// 单机
		Config config = new Config();
		config.setTransportMode(TransportMode.NIO);
		config.setCodec(JsonJacksonCodec.INSTANCE);
		config.useSingleServer().setAddress("redis://" + host + ":" + port)
				// 这里一定要处理一下无密码问题
				.setPassword(StringUtils.isEmpty(pwd) ? null : pwd).setDatabase(0);
		return Redisson.create(config);
	}
	
	private Set<Class<?>> findClient() {
		List<String> packages = AutoConfigurationPackages.get(beanFactory);
		Set<Class<?>> clientClasses = new HashSet<>();
		for (String pck : packages) {
			try {
				Set<Class<?>> clazz = PackageScans.findPackageClass(pck, RedissonFastDelayQueueClient.class);
				if (!CollectionUtils.isEmpty(clazz)) {
					clientClasses.addAll(clazz);
				}
			} catch (ClassNotFoundException | IOException e) {
			}
		}
		return clientClasses;
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
