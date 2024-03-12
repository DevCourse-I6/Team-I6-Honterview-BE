package com.i6.honterview.domain.question.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문 북마크 응답")
public record QuestionBookmarkClickResponse(

	@Schema(description = "질문 북마크 활성화 여부", example = "true")
	boolean isBookmarked
) {
}
