package com.i6.honterview.config;

import static org.springframework.http.MediaType.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GptConfig {
	@Value("${openai.secret-key}")
	private String secretKey;

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate();
		template.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add(
				"Authorization"
				, "Bearer " + secretKey);
			request.getHeaders().setContentType(APPLICATION_JSON);
			return execution.execute(request, body);
		});
		return template;
	}
}
