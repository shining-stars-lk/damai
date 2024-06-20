package com.damai.conf;


import com.damai.util.StringUtil;
import com.damai.util.BusinessEsHandle;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: elasticsearch配置
 * @author: 阿星不是程序员
 **/
@EnableConfigurationProperties(BusinessEsProperties.class)
@ConditionalOnProperty(value = "elasticsearch.ip")
public class BusinessEsAutoConfig {
	private static final int ADDRESS_LENGTH = 2;
	private static final String HTTP_SCHEME = "http";

	@Bean
	public RestClient businessEsRestClient(BusinessEsProperties businessEsProperties) {
		String defaultValue = "default";
		HttpHost[] hosts = Arrays.stream(businessEsProperties.getIp()).map(this::makeHttpHost).filter(Objects::nonNull)
				.toArray(HttpHost[]::new);
		
		RestClientBuilder builder = RestClient.builder(hosts);
		String userName = businessEsProperties.getUserName();
		String passWord = businessEsProperties.getPassWord();
		if (StringUtil.isNotEmpty(userName) && !defaultValue.equals(userName) && StringUtil.isNotEmpty(passWord) && !defaultValue.equals(passWord)) {
			//开始设置用户名和密码
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, passWord));
			builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultIOReactorConfig(IOReactorConfig.custom()
                            // 设置线程数
                            .setIoThreadCount(businessEsProperties.getMaxConnectNum()) 
                            .build()));
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
        return builder.build();
	}
	
	@Bean
	public BusinessEsHandle businessEsHandle(@Qualifier("businessEsRestClient")RestClient businessEsRestClient, BusinessEsProperties businessEsProperties){
		return new BusinessEsHandle(businessEsRestClient,businessEsProperties.getEsSwitch(),businessEsProperties.getEsTypeSwitch());
	}

	/**
	 * 获取HttpHost对象
	 *
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
