package com.i6.honterview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.request.ReissueTokenRequest;
import com.i6.honterview.dto.response.TokenResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/reissue")
	public ResponseEntity<ApiResponse> reissue(@Valid @RequestBody ReissueTokenRequest request) {
		TokenResponse reissuedToken = authService.reissue(request);
		ApiResponse<TokenResponse> response = ApiResponse.ok(reissuedToken);
		return ResponseEntity.ok(response);
	}
}
