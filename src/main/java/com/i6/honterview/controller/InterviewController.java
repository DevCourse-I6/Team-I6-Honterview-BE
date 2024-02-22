package com.i6.honterview.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.i6.honterview.dto.request.InterviewCreateRequest;
import com.i6.honterview.dto.request.InterviewUpdateRequest;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.service.InterviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "면접 연습")
@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

	private final InterviewService interviewService;

	@Operation(summary = "면접 연습 생성(면접 시작 전)")
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createInterview(
		@Valid @RequestBody InterviewCreateRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long id = interviewService.createInterview(request, userDetails.getId());
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
		return ResponseEntity.created(location).body(ApiResponse.created(id));
	}

	@Operation(summary = "면접 연습 상태 수정")
	@PatchMapping("{id}")
	public ResponseEntity<Void> updateInterviewStatus(
		@Parameter(description = "질문 id", example = "123") @PathVariable Long id,
		@Valid @RequestBody InterviewUpdateRequest request) {
		interviewService.updateInterviewStatus(id, request);
		return ResponseEntity.noContent().build();
	}

}
