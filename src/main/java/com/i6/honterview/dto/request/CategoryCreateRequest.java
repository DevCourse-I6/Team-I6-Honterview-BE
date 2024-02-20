package com.i6.honterview.dto.request;

import com.i6.honterview.domain.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 생성 요청")
public record CategoryCreateRequest(
	@Schema(description = "카테고리 이름 (1자 이상 20자 이하)", example = "프론트엔드")
	@NotBlank(message = "카테고리 명은 필수 항목입니다.")
	@Size(min = 1, max = 20, message = "카테고리 명은 1자 이상 20자 이하로 입력해주세요.")
	String categoryName
) {
	public Category toEntity() {
		return new Category(categoryName);
	}
}
