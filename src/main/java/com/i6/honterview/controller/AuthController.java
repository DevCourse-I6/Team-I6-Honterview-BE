package com.i6.honterview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.response.TokenResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.security.resolver.CurrentAccount;
import com.i6.honterview.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "토큰 재발급", description = "refresh token을 이용해 access, refresh 토큰을 재발급합니다.")
	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse<TokenResponse>> reissue(
		@CookieValue(name = "refreshToken") String refreshToken) {
		TokenResponse reissuedToken = authService.reissue(refreshToken);
		ApiResponse<TokenResponse> response = ApiResponse.ok(reissuedToken);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "로그아웃")
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logout(
		@RequestHeader("Authorization") String authorizationToken,
		@CookieValue(name = "refreshToken") String refreshToken,
		@CurrentAccount Long memberId
	) {
		authService.logout(refreshToken, authorizationToken, memberId);
		return ResponseEntity.ok(ApiResponse.ok("로그아웃 되었습니다."));
	}
}
