package com.i6.honterview.exception;

import org.springframework.http.HttpStatus;

import com.i6.honterview.response.ErrorResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// category
	CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "CATEGORY_NOT_FOUND", "존재하지 않는 카테고리입니다."),
	INVALID_CATEGORY_COUNT(HttpStatus.BAD_REQUEST, "INVALID_CATEGORY_COUNT", "카테고리 갯수는 1개 이상 3개 이하여야 합니다."),
	CATEGORY_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "CATEGORY_NAME_DUPLICATED", "중복된 카테고리 이름입니다."),

	// question
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION_NOT_FOUND", "존재하지 않는 질문입니다."),
	ORDER_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORDER_TYPE_NOT_FOUND", "지원하지 않는 정렬 방식입니다."),

	// answer
	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "ANSWER_NOT_FOUND", "존재하지 않는 답변입니다."),

	// member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "존재하지 않는 회원입니다."),

	// interview
	INTERVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "INTERVIEW_NOT_FOUND", "존재하지 않는 면접입니다."),
	INTERVIEWEE_NOT_SAME(HttpStatus.FORBIDDEN, "INTERVIEWEE_NOT_SAME", "동일한 면접자가 아닙니다."),
	INTERVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "INTERVIEW_DELETE_FORBIDDEN", "삭제할 수 없는 인터뷰입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(code, message);
	}
}
