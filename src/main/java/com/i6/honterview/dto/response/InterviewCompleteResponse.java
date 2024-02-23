package com.i6.honterview.dto.response;

import java.util.List;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.enums.InterviewStatus;
import com.i6.honterview.dto.request.InterviewCompleteRequest;

public record InterviewCompleteResponse(Long interviewId, InterviewStatus status,
										List<InterviewCompleteRequest.QuestionAnswer> questionAnswers) {
	public static InterviewCompleteResponse of(Interview interview,
		List<InterviewCompleteRequest.QuestionAnswer> questionAnswers) {
		return new InterviewCompleteResponse(interview.getId(), interview.getStatus(), questionAnswers);
	}
}
