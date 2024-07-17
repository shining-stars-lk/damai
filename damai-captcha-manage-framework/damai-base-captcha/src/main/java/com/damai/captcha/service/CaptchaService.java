/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package com.damai.captcha.service;

import com.damai.captcha.model.common.ResponseModel;
import com.damai.captcha.model.vo.CaptchaVO;

import java.util.Properties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 验证码服务接口
 * @author: 阿星不是程序员
 **/
public interface CaptchaService {
    /**
     * 配置初始化
     * @param config 配置
     */
    void init(Properties config);

    /**
     * 获取验证码
     * @param captchaVO 数据
     * @return 结果
     */
    ResponseModel get(CaptchaVO captchaVO);

    /**
     * 核对验证码(前端)
     * @param captchaVO 数据
     * @return 结果
     */
    ResponseModel check(CaptchaVO captchaVO);

    /**
     * 二次校验验证码(后端)
     * @param captchaVO 数据
     * @return 结果
     */
    ResponseModel verification(CaptchaVO captchaVO);

    /***
     * 验证码类型
     * 通过java SPI机制，接入方可自定义实现类，实现新的验证类型
     * @return 结果
     */
    String captchaType();

	/**
	 * 历史资源清除(过期的图片文件，生成的临时图片...)
	 * @param config 配置项 控制资源清理的粒度
	 */
	void destroy(Properties config);
}
