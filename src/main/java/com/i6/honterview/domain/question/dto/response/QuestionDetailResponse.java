package com.i6.honterview.domain.question.dto.response;

import java.util.List;

import com.i6.honterview.domain.answer.dto.response.AnswerResponse;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.common.dto.PageResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문 상세 응답")
public record QuestionDetailResponse(
	@Schema(description = "질문 id", example = "123")
	Long id,

	@Schema(description = "질문 내용", example = "JVM의 역할에 대해 설명해주세요.")
	String content,

	@Schema(description = "좋아요 수", example = "5")
	long heartsCount,

	@Schema(description = "카테고리 이름 목록")
	List<String> categoryNames,

	@Schema(description = "답변 목록")
	PageResponse<AnswerResponse> answers) {
	public static QuestionDetailResponse from(Question question, PageResponse<AnswerResponse> answers) {
		return new QuestionDetailResponse(
			question.getId(),
			question.getContent(),
			question.getHeartsCount(),
			question.getCategoryNames(),
			answers);
	}
}
