package com.i6.honterview.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 재발급 응답")
public record TokenResponse(String accessToken, String refreshToken) {
}
