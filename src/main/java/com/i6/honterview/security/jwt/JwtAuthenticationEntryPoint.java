package com.i6.honterview.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.i6.honterview.util.HttpResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자가 인증되지 않은 상태에서 접근하려고 할 때 발생하는 예외 처리
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		if (authException instanceof CredentialsExpiredException) {
			// 토큰이 만료된 경우
			log.warn("Token has expired");
			HttpResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "토큰의 유효기간이 만료되었습니다.");
		} else if (authException instanceof BadCredentialsException) {
			// 토큰이 없거나 인증에 실패한 경우
			log.warn("Unauthorized");
			HttpResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		} else {
			// 다른 인증 오류의 경우
			log.warn("Unauthorized");
			HttpResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증 오류가 발생했습니다");
		}
	}
}
