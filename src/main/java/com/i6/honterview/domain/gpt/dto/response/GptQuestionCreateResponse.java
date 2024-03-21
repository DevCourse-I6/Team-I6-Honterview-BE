package com.i6.honterview.domain.gpt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GptQuestionCreateResponse(
	@Schema(description = "꼬리질문 id", example = "chat-abcd")
	String id,

	@Schema(description = "생성된 꼬리질문 내용", example = "JVM은 왜 자바 바이트코드를 직접 실행시키지 않고 중간 언어로 번역해서 실행하는 것일까요?")
	String tailQuestionContent) {
	public static GptQuestionCreateResponse from(String id, Choice choice) {
		String content = refineSentence(choice.message().content());
		return new GptQuestionCreateResponse(id, content);
	}

	private static String refineSentence(String oldSentence) {
		String[] stringsToRemove = {"\"", "꼬리 질문", ":"};
		String newSentence = oldSentence;

		for (String str : stringsToRemove) {
			newSentence = newSentence.replace(str, "");
		}
		return newSentence;
	}
}
