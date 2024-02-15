package com.i6.honterview.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.i6.honterview.security.auth.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	private static final String AUTHENTICATION_CLAIM_NAME = "roles";

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.access-expiry-milliseconds}")
	private int accessExpiryMilliseconds;

	@Value("${jwt.refresh-expiry-milliseconds}")
	private int refreshExpiryMilliseconds;

	public String generateAccessToken(UserDetailsImpl userDetails) {
		return getString(userDetails, accessExpiryMilliseconds);
	}

	public String generateRefreshToken(UserDetailsImpl userDetails) {
		return getString(userDetails, refreshExpiryMilliseconds);
	}

	private String getString(UserDetailsImpl userDetails, int expiryMilliseconds) {
		Date now = new Date();
		String authorities = null;
		if (userDetails.getAuthorities() != null) {
			authorities = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		}
		return Jwts.builder()
			.issuedAt(now)
			.expiration(new Date(System.currentTimeMillis() + expiryMilliseconds))
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
	}

	public Long getMemberId(String token) {
		return Long.parseLong(verifyAndExtractClaims(token)
			.getSubject());
	}

	private SecretKey getSignInKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
}
