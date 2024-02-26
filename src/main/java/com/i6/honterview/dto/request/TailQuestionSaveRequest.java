package com.i6.honterview.dto.request;

import java.util.List;

import com.i6.honterview.domain.Category;
import com.i6.honterview.domain.Question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "꼬리 질문 저장 요청")
public record TailQuestionSaveRequest(
	@NotBlank(message = "질문 내용은 필수 항목입니다.")
	@Size(min = 2, max = 100, message = "질문 내용은 2자 이상 100자 이하로 입력해주세요.")
	String content,

	@NotNull(message = "부모 ID값은 필수 항목입니다.")
	Long parentId,

	@NotNull
	@Size(min = 1, max = 3, message = "카테고리 목록은 1개 이상 3개 이하까지 등록 가능합니다.")
	List<Long> categoryIds) {
	public Question toEntity(List<Category> categories) {
		return Question.builder()
			.content(content)
			.parentId(parentId)
			.categories(categories)
			.createdBy("GPT_CREATED")
			.build();
	}
}
