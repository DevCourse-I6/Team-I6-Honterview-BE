package com.i6.honterview.domain.question.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문 좋아요 응답")
public record QuestionHeartClickResponse(

	@Schema(description = "질문 좋아요 수", example = "123")
	long questionHeartCount,

	@Schema(description = "질문 좋아요 활성화 여부", example = "true")
	boolean isHearted
) {
}
