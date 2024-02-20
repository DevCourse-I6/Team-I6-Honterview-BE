package com.i6.honterview.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "질문 수정 요청")
public record QuestionUpdateRequest(
	@Schema(description = "질문 내용 (2자 이상 100자 이하)", example = "JVM의 역할에 대해 설명해주세요.")
	@NotBlank(message = "질문 내용은 필수 항목입니다.")
	@Size(min = 2, max = 100, message = "질문 내용은 2자 이상 100자 이하로 입력해주세요.")
	String content,
	@Schema(description = "카테고리 id 배열, 1개 이상 3개 이하까지 등록 가능", example = "[1,2,3]")
	@NotNull
	@Size(min = 1, max = 3, message = "카테고리 목록은 1개 이상 3개 이하까지 등록 가능합니다.")
	List<Long> categoryIds) {
}
