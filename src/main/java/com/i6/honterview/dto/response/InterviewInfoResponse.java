package com.i6.honterview.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record InterviewInfoResponse(
	@Schema(description = "인터뷰 ID", example = "123")
	Long interviewId,

	@Schema(description = "타이머 시간(초)", example = "90")
	int timer,

	@Schema(description = "해당 면접의 총 질문 개수", example = "3")
	int questionCount,

	@Schema(description = "해당 면접의 답변이 완료된 질문 목록")
	List<QuestionInterviewingResponse> questionAnswers,

	@Schema(description = "카테고리 목록")
	List<CategoryResponse> categories
	) {
}
