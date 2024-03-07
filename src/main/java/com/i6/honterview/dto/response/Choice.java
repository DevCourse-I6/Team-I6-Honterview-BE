package com.i6.honterview.dto.response;

import com.i6.honterview.dto.request.Message;

public record Choice(
	Message message,
	String finish_reason,
	int index
) {}
