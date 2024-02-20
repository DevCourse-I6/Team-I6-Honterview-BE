package com.i6.honterview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ReissueTokenRequest(

	@Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MDgzOTY0OTUsImV4cCI6MTcwODQ4Mjg5NSwic3ViIjoiMiIsInJvbGVzIjoiUk9MRV9VU0VSIn0.KkVlJhHXbKxi1Swgij6rITLqeBB1yWeDzI26938661YUMh-BaThIppE54D4-fchm")
	@NotBlank(message = "리프레시 토큰은 필수 항목입니다.") String refreshToken
) {
}
