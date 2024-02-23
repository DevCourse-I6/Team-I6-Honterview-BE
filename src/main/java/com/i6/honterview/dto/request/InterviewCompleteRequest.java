package com.i6.honterview.dto.request;

import java.util.List;

public record InterviewCompleteRequest(List<QuestionAnswer> questionAnswers) {

	public enum Visibility {
		PUBLIC,
		PRIVATE,
		NOT_SAVED
	}

	public record QuestionAnswer(
		Visibility visibility,
		Long questionId, // 기존 질문이면 존재, 새로운 질문이면 null? -1?
		String questionContent,
		String answerContent) {
	}
}
