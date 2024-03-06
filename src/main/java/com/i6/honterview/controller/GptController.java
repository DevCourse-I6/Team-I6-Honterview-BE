package com.i6.honterview.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.GptService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "GPT")
@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class GptController {

	private final GptService gptService;

	@PostMapping("/{interviewId}")
	public ResponseEntity<ApiResponse<GptQuestionCreateResponse>> createGptQuestion(
		@PathVariable Long interviewId,
		@Valid @RequestBody GptQuestionCreateRequest request
	) {
		GptQuestionCreateResponse response = gptService.createGptQuestion(interviewId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}
}
