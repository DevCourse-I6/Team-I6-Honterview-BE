package com.i6.honterview.domain.gpt.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.i6.honterview.config.FeignClientConfig;
import com.i6.honterview.domain.gpt.dto.request.GptApiRequest;
import com.i6.honterview.domain.gpt.dto.response.GptApiResponse;

@FeignClient(name = "gpt-client", url = "${openai.end-point}", configuration = FeignClientConfig.class)
public interface GptFeignClient {

	@PostMapping
	GptApiResponse createTailQuestion(@RequestBody GptApiRequest request);
}
