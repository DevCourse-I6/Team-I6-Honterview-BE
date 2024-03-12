package com.i6.honterview.common.exception;

import lombok.Getter;

@Getter
public class SecurityCustomException extends RuntimeException {

	private final SecurityErrorCode errorCode;

	public SecurityCustomException(SecurityErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
