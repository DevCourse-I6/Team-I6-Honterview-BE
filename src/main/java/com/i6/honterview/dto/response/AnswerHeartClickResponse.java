package com.i6.honterview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AnswerHeartClickResponse(
	@Schema(description = "답변 좋아요 활성화 여부", example = "true")
	boolean isHearted) {
}
