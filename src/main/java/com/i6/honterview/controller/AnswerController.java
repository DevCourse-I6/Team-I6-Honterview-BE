package com.i6.honterview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.response.AnswerHeartClickResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.AnswerHeartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answers")
public class AnswerController {

	private final AnswerHeartService answerHeartService;

	@PostMapping("/{id}/hearts")
	public ResponseEntity<ApiResponse> clickQuestionHeart(@PathVariable Long id) {
		AnswerHeartClickResponse response = answerHeartService.clickAnswerHeart(id, 1L);    //TODO: 회원 연동
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
