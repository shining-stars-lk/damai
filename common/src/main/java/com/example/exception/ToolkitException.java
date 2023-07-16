package com.example.exception;

import com.example.common.ApiResponse;
import com.example.enums.BaseCode;
import lombok.Data;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
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
	
	public ToolkitException(ApiResponse apiResponse) {
		super(apiResponse.getMessage());
		this.code = apiResponse.getCode();
		this.message = apiResponse.getMessage();
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
