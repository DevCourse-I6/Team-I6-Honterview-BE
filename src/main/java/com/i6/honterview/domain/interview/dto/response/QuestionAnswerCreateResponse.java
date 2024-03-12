package com.i6.honterview.domain.interview.dto.response;

import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.question.entity.Question;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "질문/답변 생성 응답")
public record QuestionAnswerCreateResponse(
	@Schema(description = "질문 id", example = "123")
	Long questionId,

	@Schema(description = "답변 id", example = "123")
	Long answerId
) {
	public static QuestionAnswerCreateResponse of(Question question, Answer answer) {
		return new QuestionAnswerCreateResponse(question.getId(), answer.getId());
	}
}
