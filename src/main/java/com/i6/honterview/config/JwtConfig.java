package com.i6.honterview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class JwtConfig {

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.access-expiry-seconds}")
	private int accessExpirySeconds;

	@Value("${jwt.refresh-expiry-seconds}")
	private int refreshExpirySeconds;
}
