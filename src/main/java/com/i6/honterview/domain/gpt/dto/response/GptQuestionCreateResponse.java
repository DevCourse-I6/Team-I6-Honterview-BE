package com.i6.honterview.domain.gpt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GptQuestionCreateResponse(
	@Schema(description = "꼬리질문 id", example = "chat-abcd")
	String id,

	@Schema(description = "생성된 꼬리질문 내용", example = "JVM은 왜 자바 바이트코드를 직접 실행시키지 않고 중간 언어로 번역해서 실행하는 것일까요?")
	String tailQuestionContent) {
	public static GptQuestionCreateResponse from(String id, Choice choice) {
		return new GptQuestionCreateResponse(id, choice.message().content());
	}
}
