package com.i6.honterview.dto.response;

import java.util.List;

import com.i6.honterview.domain.Question;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문 상세 응답")
public record QuestionDetailResponse(
	@Schema(description = "질문 id", example = "123")
	Long id,

	@Schema(description = "질문 내용", example = "JVM의 역할에 대해 설명해주세요.")
	String content,

	@Schema(description = "카테고리 이름 목록")
	List<String> categoryNames,

	@Schema(description = "답변 목록")
	PageResponse<AnswerResponse> answers) {
	public static QuestionDetailResponse from(Question question, PageResponse<AnswerResponse> answers) {
		return new QuestionDetailResponse(
			question.getId(),
			question.getContent(),
			question.getCategoryNames(),
			answers);
	}
}
