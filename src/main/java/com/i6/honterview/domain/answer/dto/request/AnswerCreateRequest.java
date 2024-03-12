package com.i6.honterview.domain.answer.dto.request;

import com.i6.honterview.domain.answer.entity.Answer;
import com.i6.honterview.domain.interview.entity.Interview;
import com.i6.honterview.domain.question.entity.Question;
import com.i6.honterview.domain.interview.entity.Video;

public record AnswerCreateRequest(
	String content
) {
	public Answer toEntity(Question question, Interview interview, Video video) {
		return new Answer(this.content, question, interview, video);
	}
}
