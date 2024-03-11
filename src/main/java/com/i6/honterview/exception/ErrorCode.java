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
	GPT_QUESTION_CREATE_FAIL(HttpStatus.BAD_REQUEST, "GPT_QUESTION_CREATE_FAIL", "문제 생성에 실패했습니다."),

	// answer
	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "ANSWER_NOT_FOUND", "존재하지 않는 답변입니다."),
	ANSWER_DUPLICATED(HttpStatus.BAD_REQUEST, "ANSWER_DUPLICATED", "답변이 이미 존재합니다."),

	// member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "존재하지 않는 회원입니다."),

	// admin
	DUPLICATED_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "DUPLICATED_MEMBER_EMAIL", "이미 가입된 이메일입니다."),
	ID_PASSWORD_MATCH_FAIL(HttpStatus.BAD_REQUEST, "ID_PASSWORD_MATCH_FAIL", "로그인 할 수 없습니다. 이메일 혹은 비밀번호를 확인해주세요."),

	// interview
	INTERVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "INTERVIEW_NOT_FOUND", "존재하지 않는 면접입니다."),
	INTERVIEWEE_NOT_SAME(HttpStatus.FORBIDDEN, "INTERVIEWEE_NOT_SAME", "동일한 면접자가 아닙니다."),
	INTERVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "INTERVIEW_DELETE_FORBIDDEN", "삭제할 수 없는 인터뷰입니다."),
	FIRST_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "FIRST_QUESTION_NOT_FOUND", "첫번째 질문이 존재하지 않습니다."),
	TOO_MANY_ANSWERS(HttpStatus.BAD_REQUEST, "TOO_MANY_ANSWERS", "면접 연습 질문 갯수를 초과하였습니다."),
	INTERVIEW_NOT_PROCESSING(HttpStatus.FORBIDDEN, "INTERVIEW_NOT_PROCESSING", "진행중인 면접이 아닙니다."),

	// file
	GENERATE_UPLOAD_URL_FAILED(HttpStatus.BAD_REQUEST, "GENERATE_UPLOAD_URL_FAILED", "파일 업로드 URL 생성에 실패하였습니다."),
	GENERATE_DOWNLOAD_URL_FAILED(HttpStatus.BAD_REQUEST, "GENERATE_DOWNLOAD_URL_FAILED", "파일 다운로드 URL 생성에 실패하였습니다."),
	VIDEO_NOT_FOUND(HttpStatus.BAD_REQUEST, "VIDEO_NOT_FOUND", "영상 기록이 존재하지 않습니다."),

	// common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "입력값을 확인해 주세요."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 에러");

	private final HttpStatus status;
	private final String code;
	private final String message;

	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(code, message);
	}
}
