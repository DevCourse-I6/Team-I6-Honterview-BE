package com.i6.honterview.dto.request;

import java.util.List;

public record GptApiRequest(

	String model,
	List<Message> messages
) {
}
