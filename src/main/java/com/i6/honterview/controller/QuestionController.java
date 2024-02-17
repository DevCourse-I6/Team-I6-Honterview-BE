package com.i6.honterview.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i6.honterview.dto.request.QuestionUpdateRequest;
import com.i6.honterview.dto.response.PageResponse;
import com.i6.honterview.dto.response.QuestionDetailResponse;
import com.i6.honterview.dto.response.QuestionHeartClickResponse;
import com.i6.honterview.dto.response.QuestionResponse;
import com.i6.honterview.response.ApiResponse;
import com.i6.honterview.service.QuestionHeartService;
import com.i6.honterview.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionHeartService questionHeartService;

	@GetMapping
	public ResponseEntity<ApiResponse> getQuestions(
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "5") int size,
		@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "categories", required = false) List<String> categoryNames,
		@RequestParam(value = "order", defaultValue = "recent") String orderType
	) {
		PageResponse<QuestionResponse> response =
			questionService.getQuestions(page, size, query, categoryNames, orderType);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getQuestionById(@PathVariable Long id) {
		QuestionDetailResponse response = questionService.getQuestionById(id);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateQuestion(
		@PathVariable Long id, @Valid @RequestBody QuestionUpdateRequest request) {
		questionService.updateQuestion(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteQuestion(@PathVariable Long id) {
		questionService.deleteQuestion(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/hearts")
	public ResponseEntity<ApiResponse> clickQuestionHeart(@PathVariable Long id) {
		QuestionHeartClickResponse response = questionHeartService.clickQuestionHeart(id, 1L);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
