package com.i6.honterview.dto.request;

import com.i6.honterview.domain.enums.InterviewStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "면접 수정 요청")
public record InterviewUpdateRequest(
	@Schema(description = "면접 상태", example = "DURING, RESULT_CHECK")
	@NotNull(message = "면접 상태는 필수 입력 항목입니다.")
	InterviewStatus status
) {
}
