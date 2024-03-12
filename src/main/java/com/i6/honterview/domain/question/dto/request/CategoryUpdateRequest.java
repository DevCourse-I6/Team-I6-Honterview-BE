package com.i6.honterview.domain.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "카테고리 수정 요청")
public record CategoryUpdateRequest(
	@Schema(description = "카테고리 이름 (1자 이상 20자 이하)", example = "프론트엔드")
	@NotBlank(message = "카테고리 명은 필수 항목입니다.")
	@Size(min = 1, max = 20, message = "카테고리 명은 1자 이상 20자 이하로 입력해주세요.")
	String categoryName
) {
}
