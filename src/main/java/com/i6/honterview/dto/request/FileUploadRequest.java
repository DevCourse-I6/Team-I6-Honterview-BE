package com.i6.honterview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "파일 업로드 요청")
public record FileUploadRequest(
	@Schema(description = "진행 시간(초단위)", example = "60")
	int processingTime
) {
}
