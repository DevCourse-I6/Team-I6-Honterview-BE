package com.i6.honterview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.response.AnswerHeartClickResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.security.auth.UserDetailsImpl;
import com.i6.honterview.service.AnswerHeartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "답변")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answers")
public class AnswerController {

	private final AnswerHeartService answerHeartService;

	@Operation(summary = "답변 좋아요/답변 좋아요 취소")
	@PostMapping("/{id}/hearts")
	public ResponseEntity<ApiResponse<AnswerHeartClickResponse>> clickAnswerHeart(
		@Parameter(description = "답변 id", example = "123")@PathVariable Long id,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		AnswerHeartClickResponse response = answerHeartService.clickAnswerHeart(id, userDetails.getId());
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
