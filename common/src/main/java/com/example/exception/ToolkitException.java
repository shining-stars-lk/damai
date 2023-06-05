package com.example.exception;

import com.example.common.Result;
import com.example.enums.BaseCode;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-03
 **/

public class ToolkitException extends BaseException {

	private Integer code;

	public ToolkitException() {
		super();
	}

	public ToolkitException(String message) {
		super(message);
	}

	public ToolkitException(Integer code, String message) {
		super(message);
		this.code = code;
	}
	
	public ToolkitException(BaseCode baseCode) {
		super(baseCode.getMsg());
		this.code = baseCode.getCode();
	}
	
	public ToolkitException(Result result) {
		super(result.getMessage());
		this.code = result.getCode();
	}

	public ToolkitException(Throwable cause) {
		super(cause);
	}

	public ToolkitException(String message, Throwable cause) {
		super(message, cause);
	}

	public ToolkitException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
}
