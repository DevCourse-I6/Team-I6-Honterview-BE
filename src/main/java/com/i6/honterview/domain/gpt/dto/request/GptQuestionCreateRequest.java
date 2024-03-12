package com.i6.honterview.domain.gpt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "꼬리질문 생성을 위한 직전 질문과 답변 정보")
public record GptQuestionCreateRequest(

	@Schema(description = "질문", example = "JPA N+1 문제에 대해 설명해주세요.")
	@NotBlank(message = "질문 내용은 필수 항목입니다.")
	@Size(min = 2, max = 100, message = "질문 내용은 2자 이상 100자 이하로 입력해주세요.")
	String prevQuestion,

	@Schema(description = "답변", example = "조회된 엔티티의 개수만큼 연관된 엔티티를 조회하기 위해 추가적인 쿼리가 발생하는 문제를 의미합니다.")
	@NotBlank(message = "답변 내용은 필수 항목입니다.")
	String prevAnswer
) {
}
