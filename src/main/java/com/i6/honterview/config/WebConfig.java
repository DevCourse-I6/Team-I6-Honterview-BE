package com.i6.honterview.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.i6.honterview.common.security.resolver.CurrentAccountArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final CurrentAccountArgumentResolver currentAccountArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentAccountArgumentResolver);
	}

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
			"http://localhost:3000",
			"https://team-i6-honterview-fe.vercel.app",
			"https://honterview.site",
			"https://www.honterview.site"
		).toArray(String[]::new);
	}
}
