package com.i6.honterview.domain.interview.dto.request;

import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "면접에 대한 질문과 답변 정보")
public record QuestionAnswerCreateRequest(

	@Schema(description = "질문", example = "JPA N+1 문제에 대해 설명해주세요.")
	@NotBlank(message = "질문 내용은 필수 항목입니다.")
	@Size(min = 2, max = 100, message = "질문 내용은 2자 이상 100자 이하로 입력해주세요.")
	String questionContent,

	@Schema(description = "답변", example = "조회된 엔티티의 개수만큼 연관된 엔티티를 조회하기 위해 추가적인 쿼리가 발생하는 문제를 의미합니다.")
	@NotBlank(message = "답변 내용은 필수 항목입니다.")
	String answerContent,

	@Schema(description = "영상 id(화상일 경우에만 존재)", example = "123 (nullable) ")
	@Nullable
	Long videoId,

	@Schema(description = "진행시간(초 단위, 화상일 경우에만 존재)", example = "60 (초단위, nullable)")
	@Nullable
	Integer processingTime
) {
}
