package com.i6.honterview.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "면접 저장 요청")
public record InterviewCompleteRequest(
	@Schema(description = "카테고리 아이디", example = "[1,2,3]")
	@NotNull
	List<Long> catogoryIds,

	@Schema(description = "질문/답변 저장 요청")
	@NotNull
	List<QuestionAnswerCreateRequest> questionAnswerRequest) {

}
