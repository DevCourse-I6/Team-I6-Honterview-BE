package com.i6.honterview.exception;

import org.springframework.http.HttpStatus;

import com.i6.honterview.response.ErrorResponse;

import lombok.Getter;

@Getter
public class OpenAiException extends RuntimeException{
	private final String code;
	private final HttpStatus status;

	public OpenAiException(ErrorResponse errorResponse, HttpStatus status) {
		super(errorResponse.getErrorMessage());
		this.code = errorResponse.getErrorCode();
		this.status = status;
	}
}
