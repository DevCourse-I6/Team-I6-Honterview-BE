package com.i6.honterview.domain.answer.dto.request;

import com.i6.honterview.domain.interview.entity.Visibility;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "답변 공개여부 변경 요청")
public record AnswerVisibilityUpdateRequest(
	@Schema(description = "답변 ID", example = "123")
	@NotNull(message = "답변 ID는 필수 입력값 입니다.")
	Long answerId,

	@Schema(description = "공개여부", example = "PUBLIC, PRIVATE")
	@NotNull(message = "공개여부는 필수 입력 항목입니다.")
	Visibility visibility) {
}
