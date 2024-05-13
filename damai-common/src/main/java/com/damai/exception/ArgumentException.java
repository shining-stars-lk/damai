/**
 * @(#)ParameterException.java 2011-12-20 Copyright 2011 it.kedacom.com, Inc.
 *                             All rights reserved.
 */

package com.damai.exception;

import lombok.Data;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 参数异常
 * @author: 阿星不是程序员
 **/

@Data
public class ArgumentException extends BaseException {

	private Integer code;
	
	private List<ArgumentError> argumentErrorList;
	
	public ArgumentException(Integer code, List<ArgumentError> argumentErrorList) {
		this.code = code;
		this.argumentErrorList = argumentErrorList;
	}

	public ArgumentException(String message) {
		super(message);
	}

	public ArgumentException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public ArgumentException(Throwable cause) {
		super(cause);
	}

	public ArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArgumentException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
}
