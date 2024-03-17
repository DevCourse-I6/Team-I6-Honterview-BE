package com.i6.honterview.common.exception;

import org.springframework.http.HttpStatus;

import com.i6.honterview.common.dto.ErrorResponse;

import lombok.Getter;

@Getter
public enum SecurityErrorCode {

	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증 오류가 발생했습니다."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED", "토큰이 만료되었습니다"),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED", "리프레시 토큰이 유효하지 않습니다. 다시 로그인 해주세요."),
	TOKEN_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "TOKEN_AUTHENTICATION_FAILED", "토큰 인증에 실패했습니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "권한이 없습니다"),
	ALREADY_LOGGED_OUT(HttpStatus.UNAUTHORIZED, "ALREADY_LOGGED_OUT", "이미 로그아웃된 사용자입니다. 재로그인 해주세요"),
	LOGOUT_FORBIDDEN(HttpStatus.FORBIDDEN, "LOGOUT_FORBIDDEN", "로그아웃 에러가 발생했습니다. 권한이 없습니다."),
	ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_NOT_FOUND", "관리자를 찾을 수 없습니다."),
	REFRESH_NOT_EXIST(HttpStatus.BAD_REQUEST, "REFRESH_NOT_EXIST", "잘못된 요청입니다. 리프레시 토큰이 존재하지 않습니다.");

	private final HttpStatus status;
	private final String errorCode;
	private final String message;

	SecurityErrorCode(HttpStatus status, String errorCode, String message) {
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}

	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(errorCode, message);
	}
}
