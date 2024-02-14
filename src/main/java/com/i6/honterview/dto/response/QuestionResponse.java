package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Question;

public record QuestionResponse(Long id, String content) {

	public static QuestionResponse from(Question question) {
		return new QuestionResponse(question.getId(), question.getContent());
	}
}
