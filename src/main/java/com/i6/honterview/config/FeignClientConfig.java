package com.i6.honterview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class FeignClientConfig {

	@Value("${openai.secret-key}")
	private String secretKey;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {
			template.header("Authorization", "Bearer " + secretKey);
			template.header("Content-Type", "application/json");
		};
	}
}
