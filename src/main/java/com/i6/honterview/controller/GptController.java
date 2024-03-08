package com.i6.honterview.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.dto.request.GptNewQuestionCreateRequest;
import com.i6.honterview.dto.request.GptQuestionCreateRequest;
import com.i6.honterview.dto.response.GptQuestionCreateResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.GptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "GPT")
@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class GptController {

	private final GptService gptService;

	@Operation(summary = "GPT 꼬리질문 생성(다음 질문 넘어갈 때 호출)")
	@PostMapping("/{interviewId}")
	public ResponseEntity<ApiResponse<GptQuestionCreateResponse>> createTailGptQuestion(
		@PathVariable Long interviewId,
		@Valid @RequestBody GptQuestionCreateRequest request
	) {
		GptQuestionCreateResponse response = gptService.createTailGptQuestion(interviewId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}

	@Operation(summary = "GPT 꼬리질문 재생성")
	@PostMapping("/{interviewId}/new")
	public ResponseEntity<ApiResponse<GptQuestionCreateResponse>> createNewGptQuestion(
		@PathVariable Long interviewId,
		@Valid @RequestBody GptNewQuestionCreateRequest request
	) {
		GptQuestionCreateResponse response = gptService.createNewGptQuestion(interviewId, request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(response));
	}
}
