package com.i6.honterview.domain.gpt.dto.response;

import com.i6.honterview.domain.gpt.dto.request.Message;

public record Choice(
	Message message,
	String finish_reason,
	int index
) {}
