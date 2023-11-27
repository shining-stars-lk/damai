package com.example.exception;

import com.example.common.ApiResponse;
import com.example.enums.BaseCode;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/
@Data
public class CookFrameException extends BaseException {

	private Integer code;
	
	private String message;

	public CookFrameException() {
		super();
	}

	public CookFrameException(String message) {
		super(message);
	}

	public CookFrameException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	public CookFrameException(BaseCode baseCode) {
		super(baseCode.getMsg());
		this.code = baseCode.getCode();
		this.message = baseCode.getMsg();
	}
	
	public CookFrameException(ApiResponse apiResponse) {
		super(apiResponse.getMessage());
		this.code = apiResponse.getCode();
		this.message = apiResponse.getMessage();
	}

	public CookFrameException(Throwable cause) {
		super(cause);
	}

	public CookFrameException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public CookFrameException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.message = message;
	}
}
