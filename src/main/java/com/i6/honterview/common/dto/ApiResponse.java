package com.i6.honterview.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 응답")
public record ApiResponse<T>(
	@Schema(description = "응답 메세지", example = "ok")
	String message,
	@Schema(description = "데이터")
	T data) {
	public static <T> ApiResponse<T> ok(T result) {
		return new ApiResponse<>("ok", result);
	}

	public static <T> ApiResponse<T> created(T result) {
		return new ApiResponse<>("created", result);
	}
}
