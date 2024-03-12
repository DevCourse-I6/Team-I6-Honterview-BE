package com.i6.honterview.domain.user.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.common.dto.ApiResponse;
import com.i6.honterview.common.security.auth.UserDetailsImpl;
import com.i6.honterview.common.util.HttpResponseUtil;
import com.i6.honterview.domain.user.dto.request.AdminSignUpRequest;
import com.i6.honterview.domain.user.dto.request.LoginRequest;
import com.i6.honterview.domain.user.dto.response.LoginUserResponse;
import com.i6.honterview.domain.user.dto.response.TokenResponse;
import com.i6.honterview.domain.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
		refreshTokenCookie.setDomain("honterview.site");

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);
		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, "로그아웃 되었습니다.");
	}

	@Operation(summary = "관리자 회원 가입")
	@PostMapping("/admin/signUp")
	public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody AdminSignUpRequest request) {
		Long id = authService.signUp(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(id));
	}

	@Operation(summary = "관리자 로그인")
	@PostMapping("/admin/login")
	public void adminLogin(
		@Valid @RequestBody LoginRequest request,
		HttpServletResponse response
	) throws IOException { // TODO: 추후 cookieUtil로 리팩토링
		TokenResponse tokenResponse = authService.adminLogin(request);
		Cookie accessTokenCookie = new Cookie("accessToken", tokenResponse.accessToken());
		Cookie refreshTokenCookie = new Cookie("refreshToken", tokenResponse.refreshToken());

		accessTokenCookie.setPath("/");
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setDomain("honterview.site");
		response.addCookie(accessTokenCookie);

		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setDomain("honterview.site");
		response.addCookie(refreshTokenCookie);

		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, tokenResponse);
	}

	@Operation(summary = "로그인한 사용자 조회")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<LoginUserResponse>> getLoginUser(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		LoginUserResponse response;
		if (userDetails != null) {
			response = new LoginUserResponse(userDetails.getId(), userDetails.getAuthorities());
		} else {
			response = null;
		}
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
