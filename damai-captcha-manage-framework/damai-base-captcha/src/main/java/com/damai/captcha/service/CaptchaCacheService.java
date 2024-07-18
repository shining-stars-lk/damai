/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package com.damai.captcha.service;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 验证码缓存接口
 * @author: 阿星不是程序员
 **/
public interface CaptchaCacheService {

	/**
	 * 设置
	 * @param key 键
	 * @param value 值
	 * @param expiresInSeconds 过期时间
	 * */
	void set(String key, String value, long expiresInSeconds);

	/**
	 * 是否存在
	 * @param key 键
	 * @return 结果
	 * */
	boolean exists(String key);
	
	/**
	 * 删除
	 * @param key 键
	 * */
	void delete(String key);
	
	/**
	 * 查询
	 * @param key 键
	 * @return 结果
	 * */
	String get(String key);


	/**
	 * 缓存类型-local/redis/memcache/..
	 * 通过java SPI机制，接入方可自定义实现类
	 * @return 结果
	 * */
	String type();

	/***
	 * 增加
	 * @param key 键
	 * @param val 值
	 * @return 结果
	 */
	default Long increment(String key, long val){
		return 0L;
	};

}
