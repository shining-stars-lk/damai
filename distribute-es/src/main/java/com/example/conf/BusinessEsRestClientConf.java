package com.example.conf;


import com.example.core.StringUtil;
import com.example.util.BusinessEsUtil;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Objects;

@EnableConfigurationProperties(BusinessEsProperties.class)
@ConditionalOnProperty(value = "elasticsearch.ip", matchIfMissing = false)
public class BusinessEsRestClientConf {
	private static final Logger logger = LoggerFactory.getLogger(BusinessEsRestClientConf.class);
	private static final int ADDRESS_LENGTH = 2;
	private static final String HTTP_SCHEME = "http";

	@Bean
	public RestClient businessEsRestClient(BusinessEsProperties businessEsProperties) {
		HttpHost[] hosts = Arrays.stream(businessEsProperties.getIp()).map(this::makeHttpHost).filter(Objects::nonNull)
				.toArray(HttpHost[]::new);
		logger.debug("hosts:{}", Arrays.toString(hosts));
		
		RestClientBuilder builder = RestClient.builder(hosts);
		String userName = businessEsProperties.getUserName();
		String passWord = businessEsProperties.getPassWord();
		if (userName != null && !"default".equals(userName) && passWord != null && !"default".equals(passWord)) {
			//开始设置用户名和密码
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, passWord));
			builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
				@Override
				public HttpAsyncClientBuilder customizeHttpClient(
						HttpAsyncClientBuilder httpClientBuilder) {
					return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
							.setDefaultIOReactorConfig(
							IOReactorConfig.custom()
									// 设置线程数
									.setIoThreadCount(businessEsProperties.getMaxConnectNum()) 
									.build());
				}
			});
		}
		Header[] defaultHeaders = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
				new BasicHeader("Role", "Read") };
		// 设置每个请求需要发送的默认headers，这样就不用在每个请求中指定它们。
		builder.setDefaultHeaders(defaultHeaders);
		// 设置相关参数
		builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
				.setConnectTimeout(businessEsProperties.getConnectTimeOut())
				.setSocketTimeout(businessEsProperties.getSocketTimeOut())
		.setConnectionRequestTimeout(businessEsProperties.getConnectionRequestTimeOut()));
		RestClient restClient = builder.build();
		return restClient;
	}
	
	@Bean
	public BusinessEsUtil businessEsUtil(@Qualifier("businessEsRestClient")RestClient businessEsRestClient, BusinessEsProperties businessEsProperties){
		return new BusinessEsUtil(businessEsRestClient,businessEsProperties.getEsSwitch(),businessEsProperties.getEsTypeSwitch());
	}

	/**
	 * 获取HTTPHOST对象
	 * 
	 * @param s
	 * @return
	 */
	private HttpHost makeHttpHost(String s) {
		assert StringUtil.isNotEmpty(s);
		String[] address = s.split(":");
		if (address.length == ADDRESS_LENGTH) {
			String ip = address[0];
			int port = Integer.parseInt(address[1]);
			return new HttpHost(ip, port, HTTP_SCHEME);
		} else {
			return null;
		}
	}

}
