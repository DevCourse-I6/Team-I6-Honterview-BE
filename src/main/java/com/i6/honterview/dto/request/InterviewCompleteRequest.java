package com.i6.honterview.dto.request;

import java.util.List;

public record InterviewCompleteRequest(
	List<Long> catogoryIds,
	List<QuestionAnswerCreateRequest> questionAnswerRequest) {

}
