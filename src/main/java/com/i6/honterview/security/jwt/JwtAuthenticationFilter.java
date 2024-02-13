package com.i6.honterview.security.jwt;

import static org.springframework.util.StringUtils.*;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHENTICATION_HEADER = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String accessToken = getToken(request);
			if (hasText(accessToken)) {
				jwtTokenProvider.validateToken(accessToken);
				Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException e) {
			logger.warn("ExpiredJwtException Occurred");
			throw new CredentialsExpiredException("토큰의 유효기간이 만료되었습니다.");
		} catch (Exception e) {
			logger.warn("JwtAuthentication Failed.");
			throw new BadCredentialsException("토큰 인증에 실패하였습니다.");
		}
		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHENTICATION_HEADER);
		if (hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_SCHEME)) {
			return bearerToken.substring(AUTHENTICATION_SCHEME.length());
		}
		return null;
	}
}
