package com.example.exception;

import com.example.common.Result;
import com.example.enums.BaseCode;
import lombok.Data;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-03
 **/
@Data
public class ToolkitException extends BaseException {

	private Integer code;
	
	private String message;

	public ToolkitException() {
		super();
	}

	public ToolkitException(String message) {
		super(message);
	}

	public ToolkitException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	public ToolkitException(BaseCode baseCode) {
		super(baseCode.getMsg());
		this.code = baseCode.getCode();
		this.message = baseCode.getMsg();
	}
	
	public ToolkitException(Result result) {
		super(result.getMessage());
		this.code = result.getCode();
		this.message = result.getMessage();
	}

	public ToolkitException(Throwable cause) {
		super(cause);
	}

	public ToolkitException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public ToolkitException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.message = message;
	}
}
