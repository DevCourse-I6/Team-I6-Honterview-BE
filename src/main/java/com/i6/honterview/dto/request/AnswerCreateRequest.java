package com.i6.honterview.dto.request;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.enums.Visibility;

public record AnswerCreateRequest(String content, Visibility visibility) {
	public Answer toEntity(Question question, Interview interview) {
		return new Answer(this.content, this.visibility, question, interview);
	}
}
