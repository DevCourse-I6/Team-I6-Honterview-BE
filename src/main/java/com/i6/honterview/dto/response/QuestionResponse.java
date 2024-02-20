package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Question;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문 응답")
public record QuestionResponse(
	@Schema(description = "질문 id", example = "123")
	Long id,

	@Schema(description = "질문 내용", example = "JVM의 역할에 대해 설명해주세요.")
	String content) {

	public static QuestionResponse from(Question question) {
		return new QuestionResponse(question.getId(), question.getContent());
	}
}
