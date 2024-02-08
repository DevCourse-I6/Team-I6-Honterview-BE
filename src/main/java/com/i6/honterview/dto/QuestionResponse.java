package com.i6.honterview.dto;

import com.i6.honterview.domain.Question;

public record QuestionResponse(Long id, String content) {

	public QuestionResponse(Question question) {
		this(question.getId(), question.getContent());
	}
}
