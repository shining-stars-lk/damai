/**
 * @(#)ParameterException.java 2011-12-20 Copyright 2011 it.kedacom.com, Inc.
 *                             All rights reserved.
 */

package com.example.exception;

import lombok.Data;

import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
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
