package com.i6.honterview.domain.gpt.dto.response;

import java.util.List;

public record GptApiResponse(
	String id,
	String object,
	long created,
	String model,
	List<Choice> choices
) {
}
