package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Answer;

public record AnswerResponse(Long id, String content, String nickname) {
	public static AnswerResponse from(Answer answer) {
		return new AnswerResponse(answer.getId(), answer.getContent(), answer.getMember().getNickname());
	}
}
