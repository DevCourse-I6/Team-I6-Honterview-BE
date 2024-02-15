package com.i6.honterview.dto.request;

import com.i6.honterview.domain.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
	@NotBlank(message = "카테고리 명은 필수 항목입니다.")
	@Size(min = 1, max = 20, message = "카테고리 명은 1자 이상 20자 이하로 입력해주세요.")
	String categoryName
) {
	public Category toEntity() {
		return new Category(categoryName);
	}
}
