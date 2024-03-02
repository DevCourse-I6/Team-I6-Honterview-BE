package com.i6.honterview.dto.request;

public record GptQuestionCreateRequest(

	String prevQuestion,
	String prevAnswer
) {
}
