package com.i6.honterview.dto.request;

import com.i6.honterview.domain.enums.Visibility;

public record QuestionAnswerCreateRequest(
	Long sequence,
	Long questionId, // 첫번째 질문일 경우 존재
	String questionContent,
	String answerContent,
	Visibility visibility) {
}
