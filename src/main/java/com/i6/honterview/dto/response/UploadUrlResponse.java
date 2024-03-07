package com.i6.honterview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "업로드 URL 응답")
public record UploadUrlResponse(
	@Schema(description = "업로드 URL", example = "https://honterview-s3-bucket.com")
	String uploadUrl
) {
}
