package com.i6.honterview.dto.response;

import java.util.List;

public record GptQuestionCreateResponse(
	String id,
	String object,
	long created,
	String model,
	List<Choice> choices
) {
}
