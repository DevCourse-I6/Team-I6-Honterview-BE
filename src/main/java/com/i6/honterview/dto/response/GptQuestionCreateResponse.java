package com.i6.honterview.dto.response;

public record GptQuestionCreateResponse(
	String id,

	String tailQuestion) {
	public static GptQuestionCreateResponse from(String id, Choice choice) {
		return new GptQuestionCreateResponse(id, choice.message().content());
	}
}
