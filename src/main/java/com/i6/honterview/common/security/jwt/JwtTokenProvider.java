package com.i6.honterview.common.security.jwt;

import static org.springframework.util.StringUtils.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.i6.honterview.common.exception.SecurityCustomException;
import com.i6.honterview.common.exception.SecurityErrorCode;
import com.i6.honterview.common.security.auth.UserDetailsImpl;
import com.i6.honterview.config.JwtConfig;
import com.i6.honterview.domain.user.service.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private static final String AUTHENTICATION_CLAIM_NAME = "roles";
	private static final String AUTHENTICATION_SCHEME = "Bearer ";
	private final RedisService redisService;
	private final JwtConfig jwtConfig;

	public String generateAccessToken(UserDetailsImpl userDetails) {
		return getString(userDetails, jwtConfig.getAccessExpirySeconds());
	}

	public String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}

	private String getString(UserDetailsImpl userDetails, int expiryMilliseconds) {
		Instant now = Instant.now();
		Instant expirationTime = now.plusSeconds(expiryMilliseconds);

		String authorities = null;
		if (userDetails.getAuthorities() != null) {
			authorities = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		}
		return Jwts.builder()
			.issuedAt(Date.from(now))
			.expiration(Date.from(expirationTime))
			.subject(userDetails.getUsername())
			.claim(AUTHENTICATION_CLAIM_NAME, authorities)
			.signWith(getSignInKey())
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = verifyAndExtractClaims(accessToken);

		Collection<? extends GrantedAuthority> authorities = null;
		if (claims.get(AUTHENTICATION_CLAIM_NAME) != null) {
			authorities = Arrays.stream(claims.get(AUTHENTICATION_CLAIM_NAME)
					.toString()
					.split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();
		}

		UserDetailsImpl principal = UserDetailsImpl.builder()
			.id(Long.parseLong(claims.getSubject()))
			.authorities(authorities)
			.build();
		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	private Claims verifyAndExtractClaims(String token) {
		return Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public void validateToken(String token) {
		Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parse(token);
		if (redisService.hasKeyBlackList(token)) {
			throw new SecurityCustomException(SecurityErrorCode.ALREADY_LOGGED_OUT);
		}
	}

	private SecretKey getSignInKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecretKey()));
	}

	public String getTokenBearer(String bearerTokenHeader) {
		if (hasText(bearerTokenHeader) && bearerTokenHeader.startsWith(AUTHENTICATION_SCHEME)) {
			return bearerTokenHeader.substring(AUTHENTICATION_SCHEME.length());
		}
		return null;
	}
}
