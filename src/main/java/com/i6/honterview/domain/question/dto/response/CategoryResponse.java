package com.i6.honterview.domain.question.dto.response;

import com.i6.honterview.domain.question.entity.Category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 응답")
public record CategoryResponse(
	@Schema(description = "카테고리 id", example = "123")
	Long id,
	@Schema(description = "카테고리 이름", example = "프론트엔드")
	String name) {
	public static CategoryResponse from(Category category) {
		return new CategoryResponse(category.getId(), category.getCategoryName());
	}
}
