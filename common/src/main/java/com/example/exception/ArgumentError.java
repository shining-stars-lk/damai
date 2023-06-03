package com.example.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArgumentError {
	
	private String argumentName;
	
	private String message;
}
