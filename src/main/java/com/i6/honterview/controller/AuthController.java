package com.i6.honterview.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.response.TokenResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.service.AuthService;
import com.i6.honterview.util.HttpResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
	public void logout(
		@RequestHeader("Authorization") String authorizationToken,
		@CookieValue(name = "refreshToken") String refreshToken,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		HttpServletResponse response
	) throws IOException {
		authService.logout(refreshToken, authorizationToken, userDetails.getId());
		Cookie accessTokenCookie = new Cookie("accessToken", null);
		accessTokenCookie.setMaxAge(0);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setDomain("honterview.site");

		Cookie refreshTokenCookie = new Cookie("refreshToken", null);
		refreshTokenCookie.setMaxAge(0);
		refreshTokenCookie.setPath("/");
		response.addCookie(accessTokenCookie);
		refreshTokenCookie.setDomain("honterview.site");

		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, "로그아웃 되었습니다.");
	}
}
