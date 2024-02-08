package com.i6.honterview.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(getAllowOrigins())
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
			.allowCredentials(true);
	}

	private String[] getAllowOrigins() {
		return Arrays.asList(
			"http://127.0.0.1:3000",
			"http://localhost:3000"
		).toArray(String[]::new);
	}
}
