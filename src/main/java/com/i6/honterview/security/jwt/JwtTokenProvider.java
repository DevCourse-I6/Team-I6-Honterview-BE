package com.i6.honterview.security.jwt;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.access-expiry-seconds}")
	private int accessExpirySeconds;

	@Value("${jwt.refresh-expiry-seconds}")
	private int refreshExpirySeconds;

	public String generateAccessToken(String email, String role) {
		Date now = new Date();
		Map<String, String> claim = generateClaim(email, role);

		return Jwts.builder()
			.issuedAt(now)
			.expiration(new Date(System.currentTimeMillis() + accessExpirySeconds))
			.claims(claim)
			.subject(email)
			.signWith(getSignInKey())
			.compact();
	}

	public String generateRefreshToken(String email, String role) {
		Date now = new Date();
		Map<String, String> claim = generateClaim(email, role);
		return Jwts.builder()
			.issuedAt(now)
			.expiration(new Date(System.currentTimeMillis() + refreshExpirySeconds))
			.claims(claim)
			.subject(email)
			.signWith(getSignInKey())
			.compact();
	}

	private static Map<String, String> generateClaim(String email, String role) {
		return Map.of(
			"email", email,
			"role", role
		);
	}

	private SecretKey getSignInKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
}
