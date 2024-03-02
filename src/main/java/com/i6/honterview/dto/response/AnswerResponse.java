package com.i6.honterview.dto.response;

import com.i6.honterview.domain.Answer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "답변 응답")
public record AnswerResponse(
	@Schema(description = "답변 id", example = "123")
	Long id,

	@Schema(description = "답변 내용", example = "JVM은 플랫폼 독립적인 코드 실행을 가능하게 합니다.")
	String content,

	@Schema(description = "답변 작성자", example = "라이언")
	String nickname,

	@Schema(description = "좋아요 수")
	long heartsCount

) {
	public static AnswerResponse from(Answer answer) {
		return new AnswerResponse(
			answer.getId(),
			answer.getContent(),
			answer.getMember().getNickname(),
			answer.getAnswerHearts().size());
	}
}
