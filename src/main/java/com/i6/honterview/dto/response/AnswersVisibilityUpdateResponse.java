package com.i6.honterview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "답변 공개여부 변경 응답")
public record AnswersVisibilityUpdateResponse(

	@Schema(description = "인터뷰 id", example = "123")
	Long interviewId
) {
}
