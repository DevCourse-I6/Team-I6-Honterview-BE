package com.i6.honterview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuestionInterviewingResponse(

	@Schema(description = "질문 id", example = "123")
	Long questionId,

	@Schema(description = "질문 내용", example = "JVM의 역할에 대해 설명해주세요.")
	String questionContent,

	@Schema(description = "실제 답변 시간(초)", example = "30")
	int processingTime
) {
}
