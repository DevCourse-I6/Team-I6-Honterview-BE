package com.i6.honterview.dto.request;

import com.i6.honterview.domain.Answer;
import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.Video;

public record AnswerCreateRequest(
	String content
) {
	public Answer toEntity(Question question, Interview interview, Video video) {
		return new Answer(this.content, question, interview, video);
	}
}
