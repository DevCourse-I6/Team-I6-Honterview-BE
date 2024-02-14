package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Question;

public record QuestionDetailResponse(Long id, String content) {
	public static QuestionDetailResponse from(Question question) {
		return new QuestionDetailResponse(question.getId(), question.getContent());
	}
}
