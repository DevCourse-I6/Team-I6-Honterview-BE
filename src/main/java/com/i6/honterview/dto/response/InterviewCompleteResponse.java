package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.enums.InterviewStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "면접 저장 응답")
public record InterviewCompleteResponse(
	@Schema(description = "인터뷰 id", example = "123")
	Long interviewId,
	@Schema(description = "면접 상태", example = "IN_COMPLETE")
	InterviewStatus status
) {
	public static InterviewCompleteResponse from(Interview interview) {
		return new InterviewCompleteResponse(interview.getId(), interview.getStatus());
	}
}
