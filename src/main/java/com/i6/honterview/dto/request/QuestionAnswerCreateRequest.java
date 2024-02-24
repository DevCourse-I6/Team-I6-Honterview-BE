package com.i6.honterview.dto.request;

import com.i6.honterview.domain.enums.Visibility;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "면접 저장 요청")
public record QuestionAnswerCreateRequest(
	@Schema(description = "질문/답변 순서", example = "1")
	Long sequence,
	@Schema(description = "첫번째 질문의 id입니다. 이후 질문일 경우 null", example = "123")
	Long questionId, // 첫번째 질문일 경우에만 존재
	@Schema(description = "질문", example = "JPA N+1 문제에 대해 설명해주세요.")
	String questionContent,
	@Schema(description = "답변", example = "조회된 엔티티의 개수만큼 연관된 엔티티를 조회하기 위해 추가적인 쿼리가 발생하는 문제를 의미합니다.")
	String answerContent,
	@Schema(description = "공개 여부(PUBLIC, PRIVATE, NOT_SAVE", example = "PUBLIC")
	Visibility visibility) {
}
