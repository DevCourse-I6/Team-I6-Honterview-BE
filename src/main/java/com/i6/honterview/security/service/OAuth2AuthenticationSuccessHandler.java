package com.i6.honterview.security.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.i6.honterview.domain.Member;
import com.i6.honterview.security.auth.OAuth2UserImpl;
import com.i6.honterview.security.jwt.JwtTokenProvider;
import com.i6.honterview.util.HttpResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		OAuth2UserImpl oAuth2User = (OAuth2UserImpl)authentication.getPrincipal();
		Member member = oAuth2User.getMember();

		String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail(), member.getRole().name());
		String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail(), member.getRole().name());

		// TODO : refresh token redis에 저장

		Map<String, Object> body = Map.of(
			"accessToken", accessToken,
			"refreshToken", refreshToken
		);

		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, body);
	}
}
