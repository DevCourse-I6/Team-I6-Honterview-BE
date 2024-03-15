package com.i6.honterview.domain.answer.dto.request;

import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.question.entity.Question;

public record AnswerCreateRequest(
	String content,

	Integer processingTime
) {
	public Answer toEntity(Question question, Interview interview) {
		return new Answer(
			this.content,
			this.processingTime,
			question,
			interview);
	}
}
