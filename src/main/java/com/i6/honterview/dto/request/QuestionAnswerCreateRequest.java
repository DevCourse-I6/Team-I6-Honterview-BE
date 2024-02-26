package com.i6.honterview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "면접에 대한 질문과 답변 정보")
public record QuestionAnswerCreateRequest(

	@Schema(description = "질문/답변 순서", example = "1")
	Long sequence,

	@Schema(description = "질문", example = "JPA N+1 문제에 대해 설명해주세요.")
	String questionContent,

	@Schema(description = "답변", example = "조회된 엔티티의 개수만큼 연관된 엔티티를 조회하기 위해 추가적인 쿼리가 발생하는 문제를 의미합니다.")
	String answerContent
) {
}
