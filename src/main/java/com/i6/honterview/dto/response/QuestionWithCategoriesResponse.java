package com.i6.honterview.dto.response;

import java.util.List;

import com.i6.honterview.domain.Question;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문 응답")
public record QuestionWithCategoriesResponse(
	@Schema(description = "질문 id", example = "123")
	Long id,

	@Schema(description = "질문 내용", example = "JVM의 역할에 대해 설명해주세요.")
	String content,

	@Schema(description = "좋아요 수", example = "5")
	long heartsCount,

	@Schema(description = "카테고리 이름 목록")
	List<String> categoryNames
) {

	public static QuestionWithCategoriesResponse from(Question question) {
		List<String> categoryNames = question.getQuestionCategories().stream()
			.map(category -> category.getCategory().getCategoryName())
			.toList();

		return new QuestionWithCategoriesResponse(
			question.getId(),
			question.getContent(),
			question.getHeartsCount(),
			categoryNames);
	}
}
