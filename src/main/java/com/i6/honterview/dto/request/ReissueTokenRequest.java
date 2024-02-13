package com.i6.honterview.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenRequest(
	@NotBlank(message = "리프레시 토큰은 필수 항목입니다.") String refreshToken
) {
}
