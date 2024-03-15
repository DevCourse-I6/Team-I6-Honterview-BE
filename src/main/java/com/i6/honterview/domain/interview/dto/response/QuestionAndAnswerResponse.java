package com.i6.honterview.domain.interview.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.question.entity.Question;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuestionAndAnswerResponse(
	@Schema(description = "질문 id", example = "123")
	Long questionId,

	@Schema(description = "질문 내용", example = "JVM의 역할에 대해 설명해주세요.")
	String questionContent,

	@Schema(description = "답변 id", example = "123")
	Long answerId,

	@Schema(description = "답변 내용", example = "JVM은 자바 바이트코드를 실행시키는 가상 머신입니다.")
	String answerContent,

	@Schema(description = "실제 답변 시간(초), null일 경우 반환X", example = "30")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Integer processingTime

) {
	public static QuestionAndAnswerResponse of(Question question, Answer answer) {
		return new QuestionAndAnswerResponse(
			question.getId(),
			question.getContent(),
			answer != null ? answer.getId() : null,
			answer != null ? answer.getContent() : null,
			answer != null ? answer.getProcessingTime() : null
		);
	}
}
