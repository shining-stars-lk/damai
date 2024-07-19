package com.damai.captcha.service.impl;

import com.damai.captcha.service.CaptchaCacheService;
import com.damai.captcha.util.CacheUtil;

import java.util.Objects;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description:  * 对于分布式部署的应用，我们建议应用自己实现CaptchaCacheService，比如用Redis，参考service/spring-boot代码示例。
 *  如果应用是单点的，也没有使用redis，那默认使用内存。
 *  内存缓存只适合单节点部署的应用，否则验证码生产与验证在节点之间信息不同步，导致失败。
 * @author: 阿星不是程序员
 **/
public class CaptchaCacheServiceMemImpl implements CaptchaCacheService {
    @Override
    public void set(String key, String value, long expiresInSeconds) {

        CacheUtil.set(key, value, expiresInSeconds);
    }

    @Override
    public boolean exists(String key) {
        return CacheUtil.exists(key);
    }

    @Override
    public void delete(String key) {
        CacheUtil.delete(key);
    }

    @Override
    public String get(String key) {
        return CacheUtil.get(key);
    }

	@Override
	public Long increment(String key, long val) {
    	Long ret = Long.parseLong(Objects.requireNonNull(CacheUtil.get(key)))+val;
		CacheUtil.set(key, String.valueOf(ret),0);
		return ret;
	}

	@Override
    public String type() {
        return "local";
    }
}
