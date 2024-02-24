package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.enums.InterviewStatus;

public record InterviewCompleteResponse(Long interviewId, InterviewStatus status
) {
	public static InterviewCompleteResponse from(Interview interview) {
		return new InterviewCompleteResponse(interview.getId(), interview.getStatus());
	}
}
