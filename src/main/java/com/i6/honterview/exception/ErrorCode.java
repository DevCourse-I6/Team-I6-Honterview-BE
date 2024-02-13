package com.i6.honterview.exception;

import org.springframework.http.HttpStatus;

import com.i6.honterview.response.ErrorResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND", "존재하지 않는 질문입니다."),


	// member
	MEMBER_NOT_FOUND( HttpStatus.NOT_FOUND, "NOT FOUND","존재하지 않는 회원입니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "REFRESH TOKEN EXPIRED", "다시 로그인 해주세요.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(code, message);
	}
}