package com.i6.honterview.dto.request;

import com.i6.honterview.domain.Interview;
import com.i6.honterview.domain.Member;
import com.i6.honterview.domain.Question;
import com.i6.honterview.domain.enums.AnswerType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "면접 생성 요청")
public record InterviewCreateRequest(
	@Schema(description = "답변 방식", example = "TEXT")
	@NotNull(message = "답변 방식은 필수 선택 항목입니다.")
	AnswerType answerType,

	@Schema(description = "질문 개수", example = "3")
	@NotNull(message = "질문 개수는 필수 선택 항목입니다.")
	@Min(value = 1, message = "값은 1 이상이어야 합니다.")
	@Max(value = 3, message = "값은 3 이하이어야 합니다.")
	int questionCount,

	@Schema(description = "타이머 시간(초)", example = "90")
	@NotNull(message = "타이머 시간은 필수 선택 항목입니다.")
	@Min(value = 10, message = "타이머 시간은 10초 이상이어야 합니다.")
	@Max(value = 600, message = "타이머 시간은 10분(600초) 이하이어야 합니다.")
	int timer,

	@Schema(description = "질문 ID", example = "123")
	@NotNull(message = "질문 ID는 필수 입력값 입니다.")
	Long questionId
) {
	public Interview toEntity(Member member, Question question) {
		return new Interview(
			answerType,
			questionCount,
			timer,
			member,
			question);
	}
}
