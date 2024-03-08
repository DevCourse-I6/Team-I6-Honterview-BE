package com.i6.honterview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "꼬리 질문 재생성을 위한 기존 질문 정보")
public record GptNewQuestionCreateRequest(
	@Schema(description = "이전 질문", example = "JPA N+1 문제에 대해 설명해주세요.")
	@NotBlank(message = "이전 질문 내용은 필수 항목입니다.")
	@Size(min = 2, max = 100, message = "질문 내용은 2자 이상 100자 이하로 입력해주세요.")
	String prevQuestion

) {
}
