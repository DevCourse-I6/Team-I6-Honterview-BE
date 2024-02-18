package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Question;

public record QuestionDetailResponse(Long id, String content, PageResponse<AnswerResponse> answers) {
	public static QuestionDetailResponse from(Question question, PageResponse<AnswerResponse> answers) {
		return new QuestionDetailResponse(question.getId(), question.getContent(), answers);
	}
}
